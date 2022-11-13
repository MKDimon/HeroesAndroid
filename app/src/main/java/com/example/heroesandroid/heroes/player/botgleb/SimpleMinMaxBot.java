package com.example.heroesandroid.heroes.player.botgleb;

import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gamelogic.GameStatus;
import com.example.heroesandroid.heroes.gui.Visualisable;
import com.example.heroesandroid.heroes.player.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Бот по стратегии "минимакс".
 **/

public class SimpleMinMaxBot extends AIBot implements Visualisable {

    /**
     * Фабрика ботов.
     **/

    public static class SimpleMinMaxBotFactory extends AIBotFactory {
        @Override
        public SimpleMinMaxBot createBot(final Fields fields) throws GameLogicException {
            return new SimpleMinMaxBot(fields);
        }

        @Override
        public SimpleMinMaxBot createBotWithConfigs(final Fields fields,
                                                    final ClientsConfigs clientsConfigs)
                                                            throws GameLogicException {
            return new SimpleMinMaxBot(fields, clientsConfigs);
        }

        @Override
        public SimpleMinMaxBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                           final int maxRecLevel) throws GameLogicException {
            return new SimpleMinMaxBot(fields, utilityFunction, maxRecLevel);
        }
    }

    public SimpleMinMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public SimpleMinMaxBot(final Fields fields, final UtilityFunction utilityFunction, final int maxRecLevel)
            throws GameLogicException {
        super(fields, utilityFunction, maxRecLevel);
    }

    public SimpleMinMaxBot(final Fields field, final ClientsConfigs clientsConfigs)
            throws GameLogicException {
        super(field, clientsConfigs);
    }

    /**
     * Получение ответа от бота.
     **/

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        try {
            final long startTime = System.currentTimeMillis();
            final List<Answer> actions = board.getPossibleMoves();
            final List<AnswerAndWin> awList = new ArrayList<>();
            for (final Answer answer : actions) {
                final Board implBoard = board.copy(answer);
                final double win = getWinByGameTree(implBoard, 1, UtilityFunctions.MIN_VALUE,
                        UtilityFunctions.MAX_VALUE);
                awList.add(new AnswerAndWin(answer, win));
            }
            System.out.println("SimpleMinMax time: " + (System.currentTimeMillis() - startTime));
            return getGreedyDecision(awList, AnswerAndWin::win).answer();

        } catch (final GameLogicException | BoardException | UnitException e) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    /**
     * Метод рекурсивно строит дерево ходов.
     * Если узел терминальный, или достигнута максимальная глубина рекурсии,
     * возвращает максимальное (или минимальное) значение функции оценки узла.
     * Таким образом, на верхний уровень пробрасывается нужная оценка состояний из нижних уровней.
     **/

    private double getWinByGameTree(final Board implBoard, final int recLevel,
                                    double alpha, double beta) throws BoardException, UnitException, GameLogicException {
        final ToDoubleFunction<AnswerAndWin> winCalculator;
        //Если сейчас ход агента, то функция полезности будет исходной,
        //если ход противника - домножим функцию на -1.
        final boolean isMax = implBoard.getCurrentPlayer() == getField();
        if (isMax) {
            winCalculator = AnswerAndWin::win;
        } else {
            winCalculator = aw -> -aw.win();
        }
        // Если состояние терминальное, и победил агент, то возвращает +большое число,
        // если победил соперник, возвращает -большое число.
        if (implBoard.getStatus() != GameStatus.GAME_PROCESS) {
            return getTerminalStateValue(implBoard);
        }
        if (recLevel >= getMaxRecLevel()) {
            // функция полезности вычисляется для агента.
            // Показывает, насколько поелзно будет ему это действие
            return getUtilityFunction().compute(implBoard, getField());
        }
        // Если состояние не терминальное, и не достигнут максимлаьынй уровень рекурсии,
        // то начинаем строить дерево из текущего состояния.
        final List<Answer> actions = implBoard.getPossibleMoves();
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Answer answer : actions) {
            final Board simBoard = implBoard.copy(answer);
            final double win = getWinByGameTree(simBoard, recLevel + 1, alpha, beta);

            // Альфа-бета отсечения
            if (isMax && win >= beta || isMax && win <= alpha) {
                return win;
            }
            if (isMax) {
                alpha = Math.max(alpha, win);
            } else {
                beta = Math.min(beta, win);
            }

            awList.add(new AnswerAndWin(answer, win));
        }

        // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому
        // корневому ответу сопоставляется значение из нижнего состяния.
        return getGreedyDecision(awList, winCalculator).win();
    }

    /**
     * Метод находит в списке awList элемент с максимальным полем win.
     * Если поиск происходит среди ответов соперника, то на поле win вешается минус. Таким образом,
     * метод находит ответ с максимальной ценностью, если ходит агент, и ответ с минимальной (для агента)
     * ценностью, если ходит соперник.
     **/

    protected AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList,
                                             final ToDoubleFunction<AnswerAndWin> winCalculator) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = winCalculator.applyAsDouble(bestAW);
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }

}

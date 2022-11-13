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
 * Бот по стратегии "ExpectiMax".
 **/

public class ExpectiMaxBot extends AIBot implements Visualisable {

    /**
     * Фабрика ботов.
     **/

    public static class ExpectiMaxBotFactory extends AIBotFactory {
        @Override
        public ExpectiMaxBot createBot(final Fields fields) throws GameLogicException {
            return new ExpectiMaxBot(fields);
        }

        @Override
        public ExpectiMaxBot createBotWithConfigs(final Fields fields,
                                                  final ClientsConfigs clientsConfigs)
                                                        throws GameLogicException {
            return new ExpectiMaxBot(fields, clientsConfigs);
        }

        @Override
        public ExpectiMaxBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                 final int maxRecLevel) throws GameLogicException {
            return new ExpectiMaxBot(fields, utilityFunction, maxRecLevel);
        }
    }

    public ExpectiMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public ExpectiMaxBot(final Fields fields, final UtilityFunction utilityFunction, final int maxRecLevel)
            throws GameLogicException {
        super(fields, utilityFunction, maxRecLevel);
    }

    public ExpectiMaxBot(final Fields field, final ClientsConfigs clientsConfigs)
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
                final double win = getWinByGameTree(implBoard, 1);
                awList.add(new AnswerAndWin(answer, win));
            }
            System.out.println("ExpectiMax bot time: " + (System.currentTimeMillis() - startTime));
            return getMaxAW(awList).answer();

        } catch (final GameLogicException | BoardException | UnitException e) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    /**
     * Метод рекурсивно строит дерево ходов.
     * Если узел терминальный, или достигнута максимальная глубина рекурсии,
     * возвращает либо максимальное значение функции оценки узла, либо "среднюю" оценку.
     * Таким образом, на верхний уровень пробрасывается нужная оценка состояний из нижних уровней.
     **/

    private double getWinByGameTree(final Board implBoard, final int recLevel)
            throws BoardException, UnitException, GameLogicException {
        final boolean isMax = implBoard.getCurrentPlayer() == getField();

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
            final double win = getWinByGameTree(simBoard, recLevel + 1);
            awList.add(new AnswerAndWin(answer, win));
        }

        // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому
        // корневому ответу сопоставляется значение из нижнего состяния.
        if (isMax) {
            return getMaxAW(awList).win();
        } else {
            return getChance(awList, aw -> aw.win() / awList.size());
        }

    }

    /**
     * Метод находит в списке awList элемент с максимальным полем win.
     **/

    protected AnswerAndWin getMaxAW(final List<AnswerAndWin> awList) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = bestAW.win();
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = currentAW.win();
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }

    /**
     * Метод оценивает вероятностные узлы с помощью функции probabilityFunction.
     **/

    protected double getChance(final List<AnswerAndWin> awList,
                               final ToDoubleFunction<AnswerAndWin> probabilityFunction) {
        double win = 0;
        for (final AnswerAndWin aw : awList) {
            win += probabilityFunction.applyAsDouble(aw);
        }
        return win;
    }

}

package com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions;

import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gamelogic.GameLogic;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.BaseBot;
import com.example.heroesandroid.heroes.player.botdimon.MonteCarloBot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class MonteCarloFunc implements IUtilityFunc {
    private final int GAMES_COUNT = 30; // 30
    private final int DEPTH = 5; // 5
    private final IUtilityFunc func = new UtilityAnswerFuncFourV2();

    private double getResult(final Board board, final Fields field) {
        try {
            final BaseBot bot1 = new MonteCarloBot.MonteCarloFactory().createBot(Fields.PLAYER_ONE);
            final BaseBot bot2 = new MonteCarloBot.MonteCarloFactory().createBot(Fields.PLAYER_TWO);

            final GameLogic gameLogic = new GameLogic(board);

            final Map<Fields, BaseBot> getPlayer = new HashMap<>();
            getPlayer.put(Fields.PLAYER_ONE, bot1);
            getPlayer.put(Fields.PLAYER_TWO, bot2);

            for (int i = 0; gameLogic.isGameBegun() && i < DEPTH;) {

                final Answer answer = getPlayer.get(gameLogic.getBoard().getCurrentPlayer())
                        .getAnswer(new Board(gameLogic.getBoard()));
                if (gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType())) {
                    i++;
                }
            }
            return func.getValue(gameLogic.getBoard(), field);
        } catch (final GameLogicException | UnitException | BoardException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        double result = 0;
        try {
            ExecutorService service = Executors.newCachedThreadPool();
            for (int i = 0; i < GAMES_COUNT; i++) {
                result += service.submit(() -> getResult(board, field)).get();
            }
            service.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}

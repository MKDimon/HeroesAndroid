package com.example.heroesandroid.heroes.player.botdimon;

import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gui.Visualisable;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.BaseBot;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.Functions;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.UtilityFuncMap;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTree;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTreeFactory;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTrees;
import com.example.heroesandroid.heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Умный бот для проверки Димона
 */
public class AntiDimon extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(AntiDimon.class);

    private final int maxHeight = 3;

    public AntiDimon(final Fields field) throws GameLogicException {
        super(field);
    }

    public static class AntiDimonFactory extends BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new AntiDimon(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new AntiDimon(fields);
        }
    }

    private int getMaxHeight(final Unit[][] army) {
        return ((int) Board.activeCount(army) <= 3)? 2 : 0;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        return null;
    }

    @Override
    public Answer getAnswer(final Board board) {
        final long start = System.currentTimeMillis();
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                SimulationTrees.EXPECTI_SIMULATION,
                UtilityFuncMap.getFunc(Functions.MONTE_CARLO),
                getField(), 1, true
        );
        final Answer answer = tree.getAnswer(board);
        final long finish = System.currentTimeMillis();
        System.out.println("ANTI TIME: " + (finish - start));
        return answer;
    }
}

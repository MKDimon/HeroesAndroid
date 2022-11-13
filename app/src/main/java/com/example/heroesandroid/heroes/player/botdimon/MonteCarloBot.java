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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Умный бот Димон
 */
public class MonteCarloBot extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(MonteCarloBot.class);

    public MonteCarloBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public static class MonteCarloFactory extends BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new MonteCarloBot(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new MonteCarloBot(fields);
        }
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
            return null;
    }

    @Override
    public Answer getAnswer(final Board board) {
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                SimulationTrees.EXPECTI_SIMULATION,
                UtilityFuncMap.getFunc(Functions.EXPONENT_FUNCTION_V2),
                getField(), 1, false
        );
        final Answer answer = tree.getAnswer(board);
        final long finish = System.currentTimeMillis();
        return answer;
    }
}

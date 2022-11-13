package com.example.heroesandroid.heroes.player.botdimon;

import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.BaseBot;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.Functions;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.UtilityFuncMap;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTree;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTreeFactory;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTrees;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreeArmy;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreesArmy;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreesArmyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Умный бот Димон
 */
public class Dimon extends BaseBot {
    private static final Logger logger = LoggerFactory.getLogger(Dimon.class);
    private final SimulationTrees tree;
    private final Functions func;
    private final int height;
    private final boolean clustering;
    private final String army;

    private static final Map<String, SimulationTrees> trees = new HashMap<>();
    private static final Map<String, Functions> functions = new HashMap<>();
    static {
        trees.put(SimulationTrees.EXPECTI_SIMULATION.name(), SimulationTrees.EXPECTI_SIMULATION);
        trees.put(SimulationTrees.ONE_STEP_SIMULATION.name(), SimulationTrees.ONE_STEP_SIMULATION);
        trees.put(SimulationTrees.CUSTOM_STEP_SIMULATION.name(), SimulationTrees.CUSTOM_STEP_SIMULATION);
        trees.put(SimulationTrees.THREAD_CUSTOM_STEP_SIMULATION.name(), SimulationTrees.THREAD_CUSTOM_STEP_SIMULATION);
        trees.put(SimulationTrees.THREAD_EXPECTI_SIMULATION.name(), SimulationTrees.THREAD_EXPECTI_SIMULATION);

        functions.put(Functions.MONTE_CARLO.name(), Functions.MONTE_CARLO);
        functions.put(Functions.SIMPLE_FUNCTION.name(), Functions.SIMPLE_FUNCTION);
        functions.put(Functions.PARAMS_FUNCTION.name(), Functions.PARAMS_FUNCTION);
        functions.put(Functions.EXPONENT_FUNCTION_V1.name(), Functions.EXPONENT_FUNCTION_V1);
        functions.put(Functions.EXPONENT_FUNCTION_V2.name(), Functions.EXPONENT_FUNCTION_V2);
        functions.put(Functions.NEURON.name(), Functions.NEURON);
    }

    public Dimon(final Fields field) throws GameLogicException {
        super(field);
        tree = null;
        func = null;
        height = 3;
        clustering = false;
        army = "const 0";
    }

    public Dimon(final Fields field, final ClientsConfigs clientsConfigs) throws GameLogicException {
        super(field);
        tree = trees.getOrDefault(clientsConfigs.SIMULATION_TREE, SimulationTrees.EXPECTI_SIMULATION);
        func = functions.getOrDefault(clientsConfigs.UTILITY_FUNC, Functions.EXPONENT_FUNCTION_V2);
        height = clientsConfigs.HEIGHT;
        clustering = clientsConfigs.CLUSTERING;
        army = clientsConfigs.ARMY;
    }

    public static class DimonFactory extends BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new Dimon(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(final Fields fields,
                                            final ClientsConfigs clientsConfigs) throws GameLogicException {
            return new Dimon(fields, clientsConfigs);
        }
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        final SimulationTreeArmy tree = new SimulationTreesArmyMap().getTree(
                SimulationTreesArmy.SIMPLE_SIMULATION
        );
        return tree.getArmy(army);
    }

    @Override
    public Answer getAnswer(final Board board) {
        final long start = System.currentTimeMillis();
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                this.tree,
                UtilityFuncMap.getFunc(this.func),
                getField(), this.height, this.clustering
        );
        final Answer answer = tree.getAnswer(board);
        final long finish = System.currentTimeMillis();
        System.out.println("TIME: " + (finish - start));
        return answer;
    }

    @Override
    public String toString() {
        return "Dimon{" +
                "tree=" + tree +
                ", func=" + func +
                ", height=" + height +
                ", clustering=" + clustering +
                ", army='" + army + '\'' +
                '}';
    }
}

package com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers;

import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Дерево симуляции на один шаг вперед
 * Ничего не может, может только дать лучший шаг
 */
public class SimulationOneStep extends SimulationTree {
    private final Logger logger = LoggerFactory.getLogger(SimulationOneStep.class);

    public SimulationOneStep(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        super(func, field, maxHeight, clustering);
    }

    @Override
    public Answer getAnswer(final Board board) {
        final Node root = new Node(board, null, Double.MIN_VALUE);
        super.setUnitAccuracy(board);
        Node maxNode = root;
        try {
            root.list.addAll(getAllSteps(root, field));
            maxNode = root.list.get(0);
            for (final Node item : root.list) {
                maxNode = (Double.compare(maxNode.value, func.getValue(item.board, field)) < 0) ? item : maxNode;
            }
        } catch (final UnitException | GameLogicException | BoardException e) {
            logger.error("Error get answer simulation", e);
        }
        return maxNode.answer;
    }

}

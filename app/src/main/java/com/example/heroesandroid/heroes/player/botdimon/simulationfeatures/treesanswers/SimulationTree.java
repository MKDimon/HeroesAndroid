package com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesanswers;

import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes;
import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gamelogic.GameLogic;
import com.example.heroesandroid.heroes.mathutils.Position;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;
import com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.functions.UtilityAnswerFuncFourV2;
import com.example.heroesandroid.heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Абстрактное дерево симуляции ответов
 *
 * Выдает все возможные ходы
 * Может менять урон юнитов на мат ожидание урона
 *
 */
public abstract class SimulationTree {
    protected final Logger logger = LoggerFactory.getLogger(SimulationTree.class);

    protected final IUtilityFunc func;
    private static final int CLUSTERS = 10;
    protected final Fields field;
    protected final int maxHeight;
    protected final boolean clustering;


    protected class Node {
        public final List<Node> list = new ArrayList<>();
        public final Board board;
        public final Answer answer;
        public double value;

        public Node(final Board board, final Answer answer, final double value) {
            this.board = board;
            this.answer = answer;
            this.value = value;
        }

        public Node(final Node node) throws UnitException, BoardException {
            this.list.addAll(node.list);
            this.board = new Board(node.board);
            this.answer = new Answer(node.answer);
            this.value = node.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Double.compare(node.value, value) == 0 && Objects.equals(list, node.list) && Objects.equals(board, node.board) && Objects.equals(answer, node.answer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(list, board, answer, value);
        }
    }

    public SimulationTree(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        this.func = func;
        this.field = field;
        this.maxHeight = maxHeight;
        this.clustering = clustering;
    }

    private boolean setAction(final Board board, final Answer answer, final List<Node> result)
            throws UnitException, BoardException {
        boolean returns = false;
        final GameLogic gameLogic = new GameLogic(board);
        if (gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType())) {
            final Board newBoard = gameLogic.getBoard();
            result.add(new Node(newBoard, answer, func.getValue(board, field)));
            if (answer.getActionType() == ActionTypes.AREA_DAMAGE) { returns = true; }
        }
        return returns;
    }


    /**
     * Меняет урон юнитов на мат ожидание урона
     * @param board состояние игры
     */
    protected void setUnitAccuracy(final Board board) {
        try {
            for (Unit[] units: board.getFieldPlayerOne()) {
                for (Unit unit: units) {
                    unit.setPower(unit.getPower() * unit.getAccuracy() / 100);
                    unit.setAccuracy(100);
                }
            }
            for (Unit[] units: board.getFieldPlayerTwo()) {
                for (Unit unit: units) {
                    unit.setPower(unit.getPower() * unit.getAccuracy() / 100);
                    unit.setAccuracy(100);
                }
            }
        } catch (UnitException e) {
            logger.error("Error set unit accuracy", e);
        }
    }

    /**
     * Возвращает все возможные варианты ходов из заданного состояния игры
     * ! В вернувшихся узлах ходы уже сделаны !
     *
     * @param root корень (узел, которому собираем потомков)
     * @param field поле игрока, который ходит
     * @return список вершин - потомков
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     * @throws GameLogicException ошибка
     */
    protected List<Node> getAllSteps(final Node root, final Fields field) throws UnitException, BoardException, GameLogicException {
        final List<Node> result = new ArrayList<>();
        final Board board = new Board(root.board);

        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Unit[][] army = board.getArmy(field);
        final Unit[][] enemies = board.getArmy(enemyField);

        final List<Position> posAttack = new ArrayList<>();
        final List<Position> posDefend = new ArrayList<>();
        final List<Position> posHealing = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (army[i][j].isActive())      { posAttack.add(new Position(i, j, field));     }
                if (army[i][j].isAlive() && army[i][j].getCurrentHP() != army[i][j].getMaxHP())
                                                { posHealing.add(new Position(i, j, field));    }
                if (enemies[i][j].isAlive())    { posDefend.add(new Position(i, j, enemyField));}
            }
        }

        for (final Position unit : posAttack) {
            final Unit un = board.getUnitByCoordinate(unit);
            final ActionTypes attackType = un.getActionType();
            final boolean isHealer = attackType.equals(ActionTypes.HEALING);

            for (final Position target : (isHealer) ? posHealing : posDefend) {
                final Answer answer = new Answer(unit, target, attackType);
                if (setAction(board, answer, result)) { break; }
            }

            final Answer defAnswer = new Answer(unit, unit, ActionTypes.DEFENSE);
            setAction(board, defAnswer, result);
        }
        return result;
    }

    public abstract Answer getAnswer(final Board board);
}

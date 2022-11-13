package com.example.heroesandroid.heroes.player.botnikita.simulation;

import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gamelogic.GameLogic;
import com.example.heroesandroid.heroes.mathutils.Position;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.botnikita.PositionUnit;
import com.example.heroesandroid.heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class BoardSimulation {
    private static final Logger logger = LoggerFactory.getLogger(BoardSimulation.class);
    public static Board simulateTurn(final Board actualBoard, final Answer answer) {
        final GameLogic gl;
        try {
            gl = new GameLogic(actualBoard);
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            return gl.getBoard();
        } catch (UnitException | BoardException e) {
            logger.error("Cannot create GameLogic object in BoardSimulation", e);
        }
        return actualBoard;
    }

    public static List<PositionUnit> getActiveUnits(final Board board, final Fields field) {
        final List<PositionUnit> positions = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                final Unit unit = board.getUnitByCoordinate(new Position(i, j, field));
                if (unit.isActive() && unit.isAlive()) {
                    final PositionUnit pair = new PositionUnit(new Position(i,j, field), unit);
                    positions.add(pair);
                }
            }
        }
        return positions;
    }
}

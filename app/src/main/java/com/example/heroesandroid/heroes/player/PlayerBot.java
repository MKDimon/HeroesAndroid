package com.example.heroesandroid.heroes.player;

import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes;
import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.mathutils.Position;
import com.example.heroesandroid.heroes.units.General;
import com.example.heroesandroid.heroes.units.GeneralTypes;
import com.example.heroesandroid.heroes.units.Unit;
import com.example.heroesandroid.heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Класс - бот, управляемый игроком.
 * Игрок через консоль задает армию и выбирает дейтсвия над юнитами.
 **/

public class PlayerBot extends BaseBot {
    private final Logger logger = LoggerFactory.getLogger(PlayerBot.class);
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Фабрика ботов.
     **/

    public static class PlayerBotFactory extends BaseBotFactory {

        @Override
        public PlayerBot createBot(final Fields fields) throws GameLogicException {
            return new PlayerBot(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new PlayerBot(fields);
        }
    }

    public PlayerBot(final Fields fields) throws GameLogicException {
        super(fields);
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        System.out.println(new StringBuffer().append("Choose your general: ").append(GeneralTypes.COMMANDER)
                .append(", ").append(GeneralTypes.ARCHMAGE).append(", ").append(GeneralTypes.SNIPER));
        General general;
        while (true) {
            try {
                general = new General(GeneralTypes.valueOf(scanner.nextLine()));
                break;
            } catch (UnitException | IllegalArgumentException e) {
                System.out.println("Incorrect general!!!");
                System.out.println(new StringBuffer().append("Choose your general: ").append(GeneralTypes.COMMANDER)
                        .append(", ").append(GeneralTypes.ARCHMAGE).append(", ").append(GeneralTypes.SNIPER));
            }
        }
        int genX;
        int genY;
        System.out.println("Choose general position: ");
        while (true) {
            try {
                System.out.print("x: ");
                genX = scanner.nextInt();
                System.out.print("y: ");
                genY = scanner.nextInt();
                if (genX > 1 || genX < 0 || genY < 0 || genY > 2) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (GameLogicException e) {
                System.out.println("Incorrect general position!!!");
                System.out.println("Choose general position: ");
            }
        }
        Unit[][] units = new Unit[2][3];
        units[genX][genY] = general;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (units[i][j] == null) {
                    System.out.println(String.format("Choose unit at (%d, %d): ", i, j));
                    while (true) {
                        try {
                            String unitTypeString = scanner.next();
                            units[i][j] = new Unit(UnitTypes.valueOf(unitTypeString));
                            break;
                        } catch (IllegalArgumentException | UnitException e) {
                            System.out.println("Incorrect unit!!!");
                            System.out.println(String.format("Choose unit at (%d, %d): ", i, j));
                        }
                    }
                }
            }
        }
        try {
            return new Army(units, general);
        } catch (BoardException | UnitException e) {
            logger.error("Error army creating");
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        int attackerX;
        int attackerY;
        System.out.println("Choose attacker position: ");
        while (true) {
            try {
                System.out.print("X: ");
                attackerX = scanner.nextInt();
                System.out.print("Y: ");
                attackerY = scanner.nextInt();
                if (!board.getUnitByCoordinate(new Position(attackerX, attackerY, getField())).isActive()) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (IllegalArgumentException | IndexOutOfBoundsException | GameLogicException e) {
                System.out.println("Incorrect attacker position!");
                System.out.println("Choose attacker position: ");

            }
        }
        final Position attacker = new Position(attackerX, attackerY, getField());
        System.out.println(new StringBuffer("Choose action: ")
                .append(board.getUnitByCoordinate(attacker).getActionType().toString())
                .append(", ").append(ActionTypes.DEFENSE));
        ActionTypes act;
        while (true) {
            try {
                String actionTypeString = scanner.next();
                act = ActionTypes.valueOf(actionTypeString);
                if (act != board.getUnitByCoordinate(attacker).getActionType() && act != ActionTypes.DEFENSE) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (IllegalArgumentException | GameLogicException e) {
                System.out.println("Incorrect action!");
                System.out.println("Choose action: ");
            }
        }
        Fields defField = (getField() == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;
        if (act == ActionTypes.DEFENSE) {
            return new Answer(attacker, attacker, ActionTypes.DEFENSE);
        }
        if (act == ActionTypes.HEALING) {
            defField = getField();
        }
        int defenderX;
        int defenderY;
        System.out.println("Choose defender position: ");
        while (true) {
            try {
                System.out.print("X: ");
                defenderX = scanner.nextInt();
                System.out.print("Y: ");
                defenderY = scanner.nextInt();
                if (!board.getUnitByCoordinate(new Position(defenderX, defenderY, getField())).isAlive()) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (IllegalArgumentException | IndexOutOfBoundsException | GameLogicException e) {
                System.out.println("Incorrect attacker position!");
                System.out.println("Choose defender position: ");
            }
        }
        Position defender = new Position(defenderX, defenderY, defField);
        return new Answer(attacker, defender, act);
    }

}

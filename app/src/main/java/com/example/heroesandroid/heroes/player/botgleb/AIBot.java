package com.example.heroesandroid.heroes.player.botgleb;

import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gamelogic.GameStatus;
import com.example.heroesandroid.heroes.gui.Visualisable;
import com.example.heroesandroid.heroes.player.BaseBot;
import com.example.heroesandroid.heroes.units.General;
import com.example.heroesandroid.heroes.units.GeneralTypes;
import com.example.heroesandroid.heroes.units.Unit;
import com.example.heroesandroid.heroes.units.UnitTypes;

public abstract class AIBot extends BaseBot implements Visualisable {
    public static final UtilityFunction baseUtilityFunction = UtilityFunctions.HPUtilityFunction;
    public static final int baseMaxRecLevel = 3;
    private final UtilityFunction utilityFunction;
    private final int maxRecLevel;

    public abstract static class AIBotFactory extends BaseBotFactory {

        @Override
        public abstract AIBot createBot(final Fields fields) throws GameLogicException;

        public abstract AIBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                          int maxRecLevel) throws GameLogicException;
        @Override
        public abstract AIBot createBotWithConfigs(final Fields field, final ClientsConfigs configs)
                throws GameLogicException;
    }

    public AIBot(final Fields field, final UtilityFunction utilityFunction, final int maxRecLevel)
            throws GameLogicException {
        super(field);
        this.utilityFunction = utilityFunction;
        this.maxRecLevel = maxRecLevel;
    }

    public AIBot(final Fields field) throws GameLogicException {
        this(field, baseUtilityFunction, baseMaxRecLevel);
    }

    public AIBot(final Fields field, final ClientsConfigs clientsConfigs)
            throws GameLogicException {
        this(field, baseUtilityFunction, clientsConfigs.HEIGHT);
    }

    public int getMaxRecLevel() {
        return maxRecLevel;
    }

    public UtilityFunction getUtilityFunction(){
        return utilityFunction;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            final General general = new General(GeneralTypes.ARCHMAGE);
            final Unit[][] army = new Unit[2][3];
            army[0][0] = new Unit(UnitTypes.SWORDSMAN);
            army[1][0] = new Unit(UnitTypes.BOWMAN);
            army[0][1] = new Unit(UnitTypes.SWORDSMAN);
            army[1][1] = general;
            army[0][2] = new Unit(UnitTypes.BOWMAN);
            army[1][2] = new Unit(UnitTypes.MAGE);
            return new Army(army, general);
        } catch (UnitException | BoardException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод вычисляет тип терминального состояния и выдает в соответствии с ним значение функции полезности
     * (+- условная бесконечность, либо DRAW_VALUE, если ничья).
     **/

    protected double getTerminalStateValue(final Board board) throws GameLogicException {
        if (board.getStatus() == GameStatus.GAME_PROCESS) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        if (board.getStatus() == GameStatus.NO_WINNERS) {
            return UtilityFunctions.DRAW_VALUE;
        }
        if (board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }
}

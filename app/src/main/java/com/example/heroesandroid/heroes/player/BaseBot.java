package com.example.heroesandroid.heroes.player;

import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gui.IGUI;
import com.example.heroesandroid.heroes.gui.Visualisable;

/**
 * Класс - базовый бот.
 **/

public abstract class BaseBot implements Visualisable {
    private Fields field;
    protected IGUI gui;

    @Override
    public void setTerminal(final IGUI gui) {
        this.gui = gui;
    }

    public abstract static class BaseBotFactory {
        public abstract BaseBot createBot(final Fields fields) throws GameLogicException;
        public abstract BaseBot createBotWithConfigs(final Fields fields, final ClientsConfigs clientsConfigs)
                throws GameLogicException;
    }

    public BaseBot(final Fields field) throws GameLogicException {
        if (field == null) {
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.field = field;
    }

    public abstract Army getArmy(final Army firstPlayerArmy);

    public abstract Answer getAnswer(final Board board) throws GameLogicException;

    public Fields getField() {
        return field;
    }

    public void setField(Fields field) {
        this.field = field;
    }
}

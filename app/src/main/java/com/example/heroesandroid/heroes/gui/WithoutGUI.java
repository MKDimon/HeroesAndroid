package com.example.heroesandroid.heroes.gui;

import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.controlsystem.Selector;

public class WithoutGUI implements IGUI {
    @Override
    public void refresh() {

    }

    @Override
    public void start() {

    }

    @Override
    public void printPlayer(final Fields field) {

    }

    @Override
    public void drawBots(final Selector selector) {

    }

    @Override
    public void drawWait() {

    }

    @Override
    public void update(final Answer answer, final Board board) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void endGame(final Data data) {

    }

    @Override
    public void continueGame(final Data data) {

    }
}

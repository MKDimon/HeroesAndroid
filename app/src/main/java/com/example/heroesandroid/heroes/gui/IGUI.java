package com.example.heroesandroid.heroes.gui;

import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.controlsystem.Selector;

public interface IGUI {
    void refresh();

    void start();

    void printPlayer(final Fields field);

    void drawBots(final Selector selector);

    void drawWait();

    void update(final Answer answer, final Board board);

    void stop();

    void clear();

    void endGame(final Data data);

    void continueGame(final Data data);
}

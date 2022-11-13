package com.example.heroesandroid.heroes.gui.heroeslanterna;

import com.googlecode.lanterna.input.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.controller.IController;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gui.IGUI;
import com.example.heroesandroid.heroes.gui.heroeslanterna.menudrawers.botchoicedrawers.BotMenuMap;
import com.example.heroesandroid.heroes.gui.heroeslanterna.menudrawers.botchoicedrawers.MenuBotDrawer;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.controlsystem.Selector;

/**
 * Обертка над LanternaWrapper, который управляет процессами запуска терминала.
 */

public class Lanterna implements IGUI, IController {
    private static final Logger logger = LoggerFactory.getLogger(Lanterna.class);

    private LanternaWrapper lw;

    /**
     * Конструктор по умолчанию. Вызывает конструктор по умолчанию класса LanternaWrapper.
     **/

    public Lanterna() {
    }

    /**
     * Обертка над методом refresh().
     **/

    @Override
    public void refresh() {
        lw.refresh();
    }

    /**
     * Обертка над методом start(). Запускает окно терминала.
     */

    @Override
    public void start() {
        try {
            this.lw = new LanternaWrapper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lw.start();
    }

    /**
     * Обертка над методом printPlayer().
     **/

    @Override
    public void printPlayer(final Fields field) {
        lw.printPlayer(field);
    }

    @Override
    public void drawBots(final Selector selector) {
        MenuBotDrawer.drawBots(lw, selector.getSelectedNumber());
    }

    @Override
    public void drawWait() {
        MenuBotDrawer.drawWait(lw);
    }

    /**
     * Обертка над методом update(). Основной метод отрисовки GUI.
     **/

    @Override
    public void update(final Answer answer, final Board board) {
        try {
            lw.update(answer, board);
        } catch (final IOException | UnitException e) {
            logger.error("Error updating by Lanterna", e);
        }
    }

    /**
     * Обертка над методом stop(). Закрывает терминал и очищает внутренний буфер Лантерны.
     **/

    @Override
    public void stop() {
        try {
            lw.stop();
        } catch (IOException e) {
            logger.error("Error Lanterna stopping", e);
        }
    }

    @Override
    public void clear() {
        lw.getScreen().clear();
    }

    @Override
    public KeyStroke pollInput() {
        try {
            return lw.getScreen().pollInput();
        } catch (IOException e) {
            logger.error("Error poll input by Lanterna");
        }
        return null;
    }

    @Override
    public int getFieldCommand() {
        return lw.updateMenu("Choose field (1-2 or 3 (any) )");
    }

    @Override
    public int getRoomCommand() {
        return lw.updateMenu("Choose room:");
    }

    @Override
    public String getBot(final Selector selector) {
        return BotMenuMap.getDrawer(selector.getSelectedNumber());
    }

    @Override
    public void endGame(final Data data) {
        LanternaEndGame.endGame(lw, data);
    }

    @Override
    public void continueGame(final Data data) {
        LanternaContinueGame.continueGame(lw, data);
    }

}

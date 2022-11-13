package com.example.heroesandroid.heroes.controller;

import com.example.heroesandroid.heroes.clientserver.ClientsConfigs;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.example.heroesandroid.heroes.player.controlsystem.Selector;

public class AutoController implements IController{
    private final ClientsConfigs cc;

    public AutoController(final ClientsConfigs cc) {
        this.cc = cc;
    }

    @Override
    public int getFieldCommand() {
        return cc.FIELD;
    }

    @Override
    public int getRoomCommand() {
        return cc.ROOM;
    }

    @Override
    public String getBot(final Selector selector) {
        return "Player";
    }

    @Override
    public KeyStroke pollInput() {
        return new KeyStroke(KeyType.Enter);
    }
}

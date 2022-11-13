package com.example.heroesandroid.heroes.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.example.heroesandroid.heroes.player.controlsystem.Selector;


public interface IController {
    int getFieldCommand();
    int getRoomCommand();
    String getBot(final Selector selector);
    KeyStroke pollInput();
}

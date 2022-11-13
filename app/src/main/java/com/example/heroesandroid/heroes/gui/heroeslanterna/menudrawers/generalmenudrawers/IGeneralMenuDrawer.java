package com.example.heroesandroid.heroes.gui.heroeslanterna.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.TerminalPosition;

import java.io.IOException;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;

public interface IGeneralMenuDrawer {
    void draw(final LanternaWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException;
}

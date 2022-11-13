package com.example.heroesandroid.heroes.gui.heroeslanterna.generaldrawers;

import java.io.IOException;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Side;

public interface IGeneralDrawer {
    void draw(final LanternaWrapper tw, final Side s) throws IOException;
}

package com.example.heroesandroid.heroes.gui.heroeslanterna.statusdrawers;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.mathutils.Pair;
import com.example.heroesandroid.heroes.units.Unit;

public interface IStatusDrawer {
    void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit);
}

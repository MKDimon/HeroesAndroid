package com.example.heroesandroid.heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.graphics.TextGraphics;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.mathutils.Pair;
import com.example.heroesandroid.heroes.units.Unit;

public class ActiveDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(Colors.LIGHTBLUE.color());

        if (unit.isActive() && unit.isAlive()) {
            tg.putString(topLeftCorner.getX(), topLeftCorner.getY() - 1, "xxxxxxxx");
        } else if (!unit.isActive() && unit.isAlive()) {
            tg.setForegroundColor(Colors.DARKESTRED.color());
            tg.putString(topLeftCorner.getX(), topLeftCorner.getY() - 1, "xxxxxxxx");
        }

        tg.setForegroundColor(Colors.WHITE.color());
    }
}

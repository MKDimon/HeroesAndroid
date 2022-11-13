package com.example.heroesandroid.heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.graphics.TextGraphics;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.mathutils.Pair;
import com.example.heroesandroid.heroes.units.Unit;

public class HealthDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(Colors.RED.color());
        int currentHP = unit.getCurrentHP();
        if (currentHP <= 0) {
            currentHP = 0;
            DeathDrawer dd = new DeathDrawer();
            dd.draw(tw, topLeftCorner, unit);
        }
        tg.putString(topLeftCorner.getX() + 1, topLeftCorner.getY() + 8, "HP: " + currentHP);

        tg.setForegroundColor(Colors.WHITE.color());
    }
}

package com.example.heroesandroid.heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.mathutils.Pair;
import com.example.heroesandroid.heroes.units.Unit;

public class DeathDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        final TextImage ti = new BasicTextImage(new TerminalSize(1, 1), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));

        for (int i = 0; i < 8; i++) {
            TextCharacter def = tw.getScreen().getFrontCharacter(topLeftCorner.getX() + i, topLeftCorner.getY() + i);
            TextCharacter temp = def.withBackgroundColor(Colors.DARKESTRED.color());
            ti.setCharacterAt(0, 0, temp);
            tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX() + i, topLeftCorner.getY() + i), ti);
        }
    }
}

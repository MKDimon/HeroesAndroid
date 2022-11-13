package com.example.heroesandroid.heroes.gui.heroeslanterna.unitdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaTextCharacterWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.mathutils.Pair;

public class BowmanDrawer implements IUnitDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        final TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));

        final LanternaTextCharacterWrapper tcw = new LanternaTextCharacterWrapper();

        ti.setCharacterAt(2, 0, tcw.getTC('(', Colors.BROWN));
        ti.setCharacterAt(2, 7, tcw.getTC('(', Colors.BROWN));
        ti.setCharacterAt(4, 2, tcw.getTC(')', Colors.BROWN));
        ti.setCharacterAt(4, 5, tcw.getTC(')', Colors.BROWN));
        ti.setCharacterAt(3, 1, tcw.getTC('\\', Colors.BROWN));
        ti.setCharacterAt(3, 6, tcw.getTC('/', Colors.BROWN));

        final TextCharacter sharp;
        if (isGeneral) {
            sharp = tcw.getTC('#', Colors.SILVER);
        } else {
            sharp = tcw.getTC('#', Colors.GREEN);
        }
        ti.setCharacterAt(0, 3, sharp);
        ti.setCharacterAt(0, 4, sharp);
        ti.setCharacterAt(1, 3, sharp);
        ti.setCharacterAt(1, 4, sharp);
        ti.setCharacterAt(2, 3, tcw.getTC('_', Colors.BROWN));
        ti.setCharacterAt(3, 3, tcw.getTC('_', Colors.BROWN));
        ti.setCharacterAt(4, 3, tcw.getTC('_', Colors.BROWN));
        ti.setCharacterAt(5, 3, tcw.getTC('_', Colors.BROWN));
        ti.setCharacterAt(6, 3, tcw.getTC('_', Colors.BROWN));

        final TextCharacter slash, backslash;
        if (isGeneral) {
            slash = tcw.getTC('/', Colors.GOLD);
            backslash = tcw.getTC('\\', Colors.GOLD);
        } else {
            slash = tcw.getTC('/', Colors.RED);
            backslash = tcw.getTC('\\', Colors.RED);
        }

        ti.setCharacterAt(7, 3, backslash);
        ti.setCharacterAt(7, 4, slash);
        return ti;
    }

    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isGeneral) {
        tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()),
                formTextImage(isGeneral));
    }
}

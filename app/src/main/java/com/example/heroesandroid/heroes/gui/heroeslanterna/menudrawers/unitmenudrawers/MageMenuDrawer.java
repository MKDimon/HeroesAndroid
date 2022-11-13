package com.example.heroesandroid.heroes.gui.heroeslanterna.menudrawers.unitmenudrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaLineDrawer;
import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.mathutils.Pair;

public class MageMenuDrawer implements IUnitMenuDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));
        TextCharacter dot = TextCharacter.DEFAULT_CHARACTER.withCharacter('.').withForegroundColor(Colors.SILVER.color());
        TextCharacter comma = TextCharacter.DEFAULT_CHARACTER.withCharacter(',').withForegroundColor(Colors.SILVER.color());
        TextCharacter equ = TextCharacter.DEFAULT_CHARACTER.withCharacter('=').withForegroundColor(Colors.SILVER.color());
        TextCharacter backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(Colors.INDIGO.color());
        TextCharacter slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.INDIGO.color());
        TextCharacter pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(Colors.INDIGO.color());
        TextCharacter apostrophe = TextCharacter.DEFAULT_CHARACTER.withCharacter('\'').withForegroundColor(Colors.BROWN.color());
        TextCharacter aqute = TextCharacter.DEFAULT_CHARACTER.withCharacter('`').withForegroundColor(Colors.BROWN.color());
        TextCharacter minus = TextCharacter.DEFAULT_CHARACTER.withCharacter('-').withForegroundColor(Colors.BROWN.color());

        ti.setCharacterAt(2, 0, dot);
        ti.setCharacterAt(3, 0, pipe);
        ti.setCharacterAt(4, 0, pipe);
        ti.setCharacterAt(5, 0, comma);

        ti.setCharacterAt(1, 1, backslash);
        ti.setCharacterAt(2, 1, dot);
        ti.setCharacterAt(3, 1, aqute);
        ti.setCharacterAt(4, 1, apostrophe);
        ti.setCharacterAt(5, 1, comma);
        ti.setCharacterAt(6, 1, slash);

        ti.setCharacterAt(2, 2, equ);
        ti.setCharacterAt(3, 2, comma);
        ti.setCharacterAt(4, 2, dot);
        ti.setCharacterAt(5, 2, equ);

        pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(Colors.BROWN.color());

        ti.setCharacterAt(1, 3, slash);
        ti.setCharacterAt(2, 3, minus);
        ti.setCharacterAt(3, 3, pipe);
        ti.setCharacterAt(4, 3, pipe);
        ti.setCharacterAt(5, 3, minus);
        ti.setCharacterAt(6, 3, backslash);

        ti.setCharacterAt(3, 4, pipe);
        ti.setCharacterAt(4, 4, pipe);

        ti.setCharacterAt(3, 5, pipe);
        ti.setCharacterAt(4, 5, pipe);

        ti.setCharacterAt(3, 6, pipe);
        ti.setCharacterAt(4, 6, pipe);

        ti.setCharacterAt(3, 7, pipe);
        ti.setCharacterAt(4, 7, pipe);

        return ti;
    }

    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isSelected) {
        tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()),
                formTextImage(isSelected));

        if (isSelected) {
            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() - 1, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.GOLD);

            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() + 8, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.GOLD);
        } else {
            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() - 1, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.WHITE);

            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() + 8, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.WHITE);
        }
    }
}

package com.example.heroesandroid.heroes.gui.heroeslanterna.utils;

import com.googlecode.lanterna.TerminalPosition;

import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.menudrawers.unitmenudrawers.UnitMenuTerminalGrid;
import com.example.heroesandroid.heroes.mathutils.Position;
import com.example.heroesandroid.heroes.units.General;
import com.example.heroesandroid.heroes.units.Unit;

public class LanternaArmyDrawer {
    public static void drawArmy(final LanternaWrapper tw, final TerminalPosition tp,
                                final Army army, final boolean withBorders, final Fields field) {
        drawArmy(tw, tp, army.getPlayerUnits(), army.getGeneral(), withBorders, field);
    }

    public static void drawArmy(final LanternaWrapper tw, final TerminalPosition tp,
                                final Unit[][] units, final General general,
                                final boolean withBorders, final Fields field) {
        final UnitMenuTerminalGrid utg = new UnitMenuTerminalGrid(tw);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                final Unit unit = units[i][j];
                if (unit != null) {
                    UnitDrawersMap.getDrawer(unit.getActionType())
                            .draw(tw, utg.getPair(new Position(i, j, field)),
                                    unit == general);
                }
            }
        }

    }
}

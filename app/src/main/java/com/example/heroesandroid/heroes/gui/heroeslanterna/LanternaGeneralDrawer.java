package com.example.heroesandroid.heroes.gui.heroeslanterna;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.io.IOException;
import java.util.EnumSet;

import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.GeneralDrawersMap;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.GeneralNamesMap;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Side;
import com.example.heroesandroid.heroes.units.General;

/**
 * Статический класс для рисования портретов генералов.
 */
public class LanternaGeneralDrawer {
    /**
     * Рисует портреты соответствующих каждой армии генералов.
     *
     * @param tw      экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param general ActionType генерала. Так как у всех генералов разный ActionType, то можно
     *                дифференцировать их именно по этому параметру.
     * @param side    Сторона, на которой будет отрисован портрет.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public static void drawGeneral(final LanternaWrapper tw,
                                   final General general, final Side side) throws IOException {
        GeneralDrawersMap.getDrawer(general.getActionType()).draw(tw, side);
        drawStatus(tw, general, side);
    }

    private static void drawStatus(final LanternaWrapper tw, final General general, final Side side) {
        final int x_start = (side == Side.RHS) ? tw.getTerminalSize().getColumns() - 34 : 1;
        final int y_start = 25;
        final TextGraphics tg = tw.newTG();
        tg.setForegroundColor(Colors.GOLD.color());
        tg.setModifiers(EnumSet.of(SGR.ITALIC));
        tg.putString(x_start + 13, y_start - 2, GeneralNamesMap.getName(general.getActionType()));
        tg.setForegroundColor(Colors.WHITE.color());
    }
}

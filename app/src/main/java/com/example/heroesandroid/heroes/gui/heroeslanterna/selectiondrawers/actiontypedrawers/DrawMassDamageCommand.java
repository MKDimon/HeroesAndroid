package com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers.LanternaSelectionDrawer;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.UnitTerminalGrid;
import com.example.heroesandroid.heroes.mathutils.Position;

public class DrawMassDamageCommand extends DrawCommand {
    public DrawMassDamageCommand(final LanternaWrapper tw, final Position att, final Position def) {
        super(tw, att, def);
    }

    @Override
    public void execute() {
        UnitTerminalGrid utg = new UnitTerminalGrid(super.getTw());
        LanternaSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getAtt()), '|', Colors.GREEN);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                LanternaSelectionDrawer.drawSelection(super.getTw(),
                        utg.getPair(new Position(i, j, super.getDef().F())), '|', Colors.DARKESTRED);
            }
        }
    }
}

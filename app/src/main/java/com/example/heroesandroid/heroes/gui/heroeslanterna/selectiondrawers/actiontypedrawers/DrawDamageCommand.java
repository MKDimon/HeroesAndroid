package com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers.LanternaSelectionDrawer;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.Colors;
import com.example.heroesandroid.heroes.gui.heroeslanterna.utils.UnitTerminalGrid;
import com.example.heroesandroid.heroes.mathutils.Position;

public class DrawDamageCommand extends DrawCommand {

    public DrawDamageCommand(final LanternaWrapper tw, final Position att, final Position def) {
        super(tw, att, def);
    }

    @Override
    public void execute() {
        UnitTerminalGrid utg = new UnitTerminalGrid(super.getTw());

        LanternaSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getAtt()), '|', Colors.GREEN);
        LanternaSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getDef()), '|', Colors.DARKESTRED);
    }
}

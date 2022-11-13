package com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.mathutils.Position;


public abstract class DrawCommand {
    private final Position att;
    private final Position def;
    private final LanternaWrapper tw;

    public Position getAtt() {
        return att;
    }

    public Position getDef() {
        return def;
    }

    public LanternaWrapper getTw() {
        return tw;
    }

    public DrawCommand(final LanternaWrapper tw, final Position att, final Position def) {
        this.tw = tw;
        this.att = att;
        this.def = def;
    }

    public abstract void execute();
}

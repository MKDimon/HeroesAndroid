package com.example.heroesandroid.heroes.gamelogic.validation;

import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes;
import com.example.heroesandroid.heroes.mathutils.Position;

public class AreaDamageChecker extends Checker {
    public AreaDamageChecker(final Position attacker, final Position defender,
                             final ActionTypes action, final int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return getDefender().F() != getAttacker().F();
    }
}

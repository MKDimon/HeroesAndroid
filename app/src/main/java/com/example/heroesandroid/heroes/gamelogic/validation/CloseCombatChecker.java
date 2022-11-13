package com.example.heroesandroid.heroes.gamelogic.validation;

import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes;
import com.example.heroesandroid.heroes.mathutils.Position;

public class CloseCombatChecker extends Checker {
    public CloseCombatChecker(final Position attacker, final Position defender,
                              final ActionTypes action, final int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return ((getAttacker().F() != getDefender().F() && getAttacker().X() == 0 && getDefender().X() == 0) &&
                (Math.abs(getAttacker().Y() - getDefender().Y()) < 2 || getCountLineAlive() == 1));
    }
}

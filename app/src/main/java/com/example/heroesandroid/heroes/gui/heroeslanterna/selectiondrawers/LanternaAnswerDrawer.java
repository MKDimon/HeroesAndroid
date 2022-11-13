package com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers;

import com.example.heroesandroid.heroes.gui.heroeslanterna.LanternaWrapper;
import com.example.heroesandroid.heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers.DrawCommandMap;
import com.example.heroesandroid.heroes.player.Answer;

public class LanternaAnswerDrawer {
    public static void drawAnswer(final LanternaWrapper tw, final Answer answer) {
        final DrawCommandMap drawCommandMap = new DrawCommandMap();
        drawCommandMap.getCommand(tw, answer.getAttacker(), answer.getDefender(), answer.getActionType()).execute();
    }
}

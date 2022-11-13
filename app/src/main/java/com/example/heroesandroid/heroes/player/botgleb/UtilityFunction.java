package com.example.heroesandroid.heroes.player.botgleb;

import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.gamelogic.Fields;

@FunctionalInterface
public interface UtilityFunction {
    double compute(final Board board, final Fields player);
}
package com.example.heroesandroid.heroes.player.botnikita.utilityfunction;

import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.player.Answer;

public interface IUtilityFunction {
    double evaluate(final Board actualBoard, final Answer answer);
}

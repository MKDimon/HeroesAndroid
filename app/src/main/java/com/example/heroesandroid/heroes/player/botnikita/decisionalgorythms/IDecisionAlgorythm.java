package com.example.heroesandroid.heroes.player.botnikita.decisionalgorythms;

import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.player.Answer;
import com.example.heroesandroid.heroes.player.botnikita.utilityfunction.IUtilityFunction;

public interface IDecisionAlgorythm {
     Answer getAnswer(final Board board, final IUtilityFunction utilityFunction);
}

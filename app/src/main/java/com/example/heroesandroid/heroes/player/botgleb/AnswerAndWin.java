package com.example.heroesandroid.heroes.player.botgleb;

import com.example.heroesandroid.heroes.player.Answer;

public class AnswerAndWin {
    private final Answer answer;
    private final double win;

    public AnswerAndWin(final Answer answer, final double win) {
        this.answer = answer;
        this.win = win;
    }

    public Answer answer() {
        return answer;
    }

    public double win() {
        return win;
    }
}

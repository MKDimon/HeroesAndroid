package com.example.heroesandroid.heroes.player.botnikita;

import com.example.heroesandroid.heroes.player.Answer;

public class AnswerWinHolder {
    private final Answer answer;
    private final double win;

    public AnswerWinHolder(Answer answer, double win) {
        this.answer = answer;
        this.win = win;
    }

    public Answer getAnswer() {
        return answer;
    }

    public double getWin() {
        return win;
    }
}

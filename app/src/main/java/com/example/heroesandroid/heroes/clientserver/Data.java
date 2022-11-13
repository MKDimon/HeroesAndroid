package com.example.heroesandroid.heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.commands.CommonCommands;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Board;
import com.example.heroesandroid.heroes.player.Answer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    @JsonProperty
    public final CommonCommands command;
    @JsonProperty
    public final Army army;
    @JsonProperty
    public final Board board;
    @JsonProperty
    public final Answer answer;
    @JsonProperty
    public final int info;
    @JsonProperty
    public final String botName;

    @JsonCreator
    public Data(@JsonProperty("command") final CommonCommands command, @JsonProperty("oneArmy") final Army army,
                @JsonProperty("board") final Board board, @JsonProperty("answer") final Answer answer,
                @JsonProperty("info") final int info, @JsonProperty("botName") final String botName) {
        this.command = command;
        this.army = army;
        this.board = board;
        this.answer = answer;
        this.info = info;
        this.botName = botName;
    }

    public Data(final CommonCommands command, final Board board, final Answer answer) {
        this.command = command;
        this.army = null;
        this.board = board;
        this.answer = answer;
        this.info = 0;
        botName = null;
    }

    /**
     * Вспомогательный для снятия команды
     *
     * @param data - данные
     */
    public Data(final Data data) throws UnitException, BoardException {
        command = null;
        army = (data.army != null) ? new Army(data.army) : null;
        board = (data.board != null) ? new Board(data.board) : null;
        answer = (data.answer != null) ? new Answer(data.answer) : null;
        this.info = 0;
        botName = null;
    }

    public Data(final CommonCommands command) {
        this.command = command;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = 0;
        botName = null;
    }

    public Data(final CommonCommands command, final int info) {
        this.command = command;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = info;
        botName = null;
    }

    public Data(final CommonCommands command, final Board board) {
        this.command = command;
        this.army = null;
        this.board = board;
        this.answer = null;
        this.info = 0;
        botName = null;
    }

    public Data(final Answer answer) {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = answer;
        this.info = 0;
        botName = null;
    }

    public Data(final CommonCommands command, Army one) {
        this.command = command;
        this.army = one;
        this.board = null;
        this.answer = null;
        this.info = 0;
        botName = null;
    }

    public Data() {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = 0;
        botName = null;
    }

    public Data(final int info) {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = info;
        botName = null;
    }

    public Data(final String botName) {
        command = null;
        army = null;
        board = null;
        answer = null;
        info = 0;
        this.botName = botName;

    }
}

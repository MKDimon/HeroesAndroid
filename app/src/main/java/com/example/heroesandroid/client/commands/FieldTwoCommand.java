package com.example.heroesandroid.client.commands;

import com.example.heroesandroid.WarActivity;
import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.gamelogic.Fields;

import java.io.BufferedWriter;

public class FieldTwoCommand extends Command{

    public FieldTwoCommand(final Data data, final BufferedWriter out, final WarActivity client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        super.getClient().chooseBot(Fields.PLAYER_TWO);
    }
}

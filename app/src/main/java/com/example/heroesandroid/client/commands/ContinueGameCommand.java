package com.example.heroesandroid.client.commands;

import com.example.heroesandroid.WarActivity;
import com.example.heroesandroid.heroes.clientserver.Data;

import java.io.BufferedWriter;

public class ContinueGameCommand extends Command{
    public ContinueGameCommand(final Data data, final BufferedWriter out, final WarActivity client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        getClient().getGUI().continueGame(getData());
    }
}

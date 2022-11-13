package com.example.heroesandroid.client.commands;

import com.example.heroesandroid.WarActivity;
import com.example.heroesandroid.heroes.clientserver.Data;

import java.io.BufferedWriter;

public abstract class Command {
    private final BufferedWriter out;
    private final Data data;

    public BufferedWriter getOut() {
        return out;
    }

    public Data getData() {
        return data;
    }

    public WarActivity getClient() {
        return client;
    }

    private final WarActivity client;

    public Command(final Data data, final BufferedWriter out, final WarActivity client) {
        this.out = out;
        this.data = data;
        this.client = client;
    }

    public abstract void execute();
}

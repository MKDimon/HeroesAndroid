package com.example.heroesandroid.heroes.boardfactory;

import com.example.heroesandroid.heroes.units.Unit;

import java.util.List;

public class DoNothingCommand extends Command {
    public DoNothingCommand(final Unit att, final List<Unit> def) {
        super(att, def);

    }

    @Override
    public void execute() {

    }
}

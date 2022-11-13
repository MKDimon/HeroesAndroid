package com.example.heroesandroid.client.commands;

import com.example.heroesandroid.WarActivity;
import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.commands.CommonCommands;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<CommonCommands, Command> commandMap;

    public CommandFactory() {
        commandMap = new HashMap<>();
    }

    private void initialize(final Data data, final BufferedWriter out, final WarActivity client) {
        commandMap.put(CommonCommands.DRAW, new DrawCommand(data, out, client));
        commandMap.put(CommonCommands.END_GAME, new EndGameCommand(data, out, client));
        commandMap.put(CommonCommands.CONTINUE_GAME, new ContinueGameCommand(data, out, client));
        commandMap.put(CommonCommands.GET_ANSWER, new GetAnswerCommand(data, out, client));
        commandMap.put(CommonCommands.GET_ARMY, new GetArmyCommand(data, out, client));
        commandMap.put(CommonCommands.GET_ROOM, new GetRoomCommand(data, out, client));
        commandMap.put(CommonCommands.GET_FIELD, new GetFieldCommand(data, out, client));
        commandMap.put(CommonCommands.FIELD_ONE, new FieldOneCommand(data, out, client));
        commandMap.put(CommonCommands.FIELD_TWO, new FieldTwoCommand(data, out, client));
        commandMap.put(CommonCommands.MAX_ROOMS, new MaxRoomsCommand(data, out, client));
        commandMap.put(CommonCommands.GET_BOT_NAME, new GetBotNameCommand(data, out, client));
    }

    public Command getCommand(final Data data, final BufferedWriter out, final WarActivity client) {
        initialize(data, out, client);
        return commandMap.getOrDefault(data.command, new DoNothingCommand(data, out, client));
    }
}

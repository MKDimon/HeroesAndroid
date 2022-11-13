package com.example.heroesandroid.client.commands;

import com.example.heroesandroid.WarActivity;
import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.clientserver.Serializer;
import com.example.heroesandroid.heroes.clientserver.serverexcetions.ServerExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetRoomCommand extends Command{
    private final Logger logger = LoggerFactory.getLogger(GetRoomCommand.class);

    public GetRoomCommand(final Data data, final BufferedWriter out, final WarActivity client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            int id;
            do {
                id = getClient().getController1().getRoomCommand();
            } while (id > getData().info || id < 0);
            getOut().write( Serializer.serializeData(new Data(id)) + '\n');
            getOut().flush();
        } catch (final IOException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }
}

package com.example.heroesandroid.client.commands;

import com.example.heroesandroid.WarActivity;
import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.clientserver.Serializer;
import com.example.heroesandroid.heroes.clientserver.serverexcetions.ServerExceptionType;
import com.example.heroesandroid.heroes.commands.CommonCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class DrawCommand extends Command{
    private final Logger logger = LoggerFactory.getLogger(DrawCommand.class);

    public DrawCommand(final Data data, final BufferedWriter out, final WarActivity client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            getClient().getGUI().update(getData().answer, getData().board);
            getClient().getGUI().printPlayer(getClient().getPlayer().getField());
            getOut().write(Serializer.serializeData(new Data(CommonCommands.DRAW_SUCCESSFUL)) + '\n');
        } catch (final IOException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
            try {
                getOut().write(Serializer.serializeData(new Data(CommonCommands.DRAW_UNSUCCESSFUL)) + '\n');
            } catch (final IOException ioException) {
                logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), ioException);
            }
        }
        finally {
            try {
                getOut().flush();
            } catch (final IOException e) {
                logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
            }
        }
    }
}

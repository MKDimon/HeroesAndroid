package com.example.heroesandroid.heroes;

import com.example.heroesandroid.heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.clientserver.Data;
import com.example.heroesandroid.heroes.commands.CommonCommands;
import com.example.heroesandroid.heroes.controller.IController;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.gamelogic.Fields;
import com.example.heroesandroid.heroes.gamelogic.GameLogic;
import com.example.heroesandroid.heroes.gamelogic.GameStatus;
import com.example.heroesandroid.heroes.gui.IGUI;
import com.example.heroesandroid.heroes.gui.heroeslanterna.Lanterna;
import com.example.heroesandroid.heroes.player.*;
import com.example.heroesandroid.heroes.player.botnikita.NikitaBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);

    public static void main(final String[] args) throws GameLogicException, IOException, InterruptedException, UnitException {
        final Lanterna lanterna = new Lanterna();
        final IGUI gui = lanterna;
        gui.start();
        final IController controller = lanterna;

        List<BaseBot.BaseBotFactory> factories = Arrays.asList(new RandomBot.RandomBotFactory(),
                new TestBot.TestBotFactory(), new PlayerBot.PlayerBotFactory(), //new PlayerGUIBot.PlayerGUIBotFactory(),
                new NikitaBot.NikitaBotFactory());
        BaseBot playerOne = factories.get(1).createBot(Fields.PLAYER_ONE);
        playerOne.setTerminal(gui);
        BaseBot playerTWO = factories.get(3).createBot(Fields.PLAYER_TWO);
        playerTWO.setTerminal(gui);
        Map<Fields, BaseBot> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTWO);

        int counter = 0;
        final int gamesCount = 1;
        GameLogic gl = new GameLogic();
        controller.getFieldCommand();
        for (int i = 0; i < gamesCount; i ++) {
            gl = new GameLogic();
            final Army firstPlayerArmy = playerOne.getArmy(null);
            gl.gameStart(firstPlayerArmy, playerTWO.getArmy(firstPlayerArmy));

            gui.update(null, gl.getBoard());

            while (gl.isGameBegun()) {
                TimeUnit.MICROSECONDS.sleep(500000);
                Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
                gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
                gui.update(answer, gl.getBoard());
            }
            if (gl.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS ||
                    gl.getBoard().getStatus() == GameStatus.NO_WINNERS) {
                counter = counter + 1;
            }
            System.out.println("Game number " + i);
        }

        Data data = new Data(CommonCommands.DRAW ,gl.getBoard());
        gui.endGame(data);
        System.out.println("Minmax bot won " + counter + " games of " + gamesCount);
    }
}

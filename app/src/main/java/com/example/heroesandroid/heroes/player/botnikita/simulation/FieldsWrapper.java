package com.example.heroesandroid.heroes.player.botnikita.simulation;

import com.example.heroesandroid.heroes.gamelogic.Fields;

public class FieldsWrapper {
    public static Fields getOppField(final Fields field) {
        return field == Fields.PLAYER_ONE ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;
    }
}

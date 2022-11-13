package com.example.heroesandroid.heroes.gui.heroeslanterna.utils;

import java.util.Map;

import com.example.heroesandroid.heroes.auxiliaryclasses.ActionTypes;

public class GeneralNamesMap {
    private static final Map<ActionTypes, String> generalDrawersMap;

    static {
        generalDrawersMap = Map.of(
                ActionTypes.CLOSE_COMBAT, "COMMANDER",
                ActionTypes.RANGE_COMBAT, "SNIPER",
                ActionTypes.AREA_DAMAGE, "ARCHMAGE"
        );
    }

    public static String getName(final ActionTypes at) {
        return generalDrawersMap.get(at);
    }


}

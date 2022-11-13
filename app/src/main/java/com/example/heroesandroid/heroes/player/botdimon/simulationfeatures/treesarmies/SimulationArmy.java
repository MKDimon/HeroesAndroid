package com.example.heroesandroid.heroes.player.botdimon.simulationfeatures.treesarmies;

import com.example.heroesandroid.heroes.auxiliaryclasses.boardexception.BoardException;
import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.gamelogic.Army;
import com.example.heroesandroid.heroes.units.General;
import com.example.heroesandroid.heroes.units.GeneralTypes;
import com.example.heroesandroid.heroes.units.Unit;
import com.example.heroesandroid.heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SimulationArmy extends SimulationTreeArmy {
    private static final Logger logger = LoggerFactory.getLogger(SimulationArmy.class);

    private static final Map<String, Unit> units = new HashMap<>();
    private static final Map<String, General> generals = new HashMap<>();

    static {
        try {
            units.put("SM", new Unit(UnitTypes.SWORDSMAN));
            units.put("BM", new Unit(UnitTypes.BOWMAN));
            units.put("HL", new Unit(UnitTypes.HEALER));
            units.put("MG", new Unit(UnitTypes.MAGE));
            generals.put("CM", new General(GeneralTypes.COMMANDER));
            generals.put("SN", new General(GeneralTypes.SNIPER));
            generals.put("AM", new General(GeneralTypes.ARCHMAGE));
        } catch (UnitException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Army getArmyByArmy(final String army) throws IOException, UnitException, BoardException {
        String[] unitsName = army.toUpperCase(Locale.ROOT).replace(" ", "").split(",");
        if (unitsName.length != 6) {
            throw new IOException("Incorrect config for army");
        }

        final Unit[][] armyUnits = new Unit[2][3];
        General general = null;

        for (int i = 0; i < unitsName.length; i++) {
            if (!units.containsKey(unitsName[i])) {
                general = generals.getOrDefault(unitsName[i], null);
                armyUnits[i / 3][i % 3] = general;
            }
            else {
                armyUnits[i / 3][i % 3] = units.get(unitsName[i]);
            }
        }

        return new Army(armyUnits, general);
    }
}

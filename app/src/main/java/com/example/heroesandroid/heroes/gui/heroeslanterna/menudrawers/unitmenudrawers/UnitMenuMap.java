package com.example.heroesandroid.heroes.gui.heroeslanterna.menudrawers.unitmenudrawers;

import java.util.HashMap;
import java.util.Map;

import com.example.heroesandroid.heroes.auxiliaryclasses.unitexception.UnitException;
import com.example.heroesandroid.heroes.units.Unit;
import com.example.heroesandroid.heroes.units.UnitTypes;

/**
 * Статический класс хранит неизменяемый Map, который создает связь между типом генерала и соответствующим классом
 * Drawer. Эти классы рисуют портреты генералов.
 * По сути, вместе с IGeneralDrawer, это урезанная реализация паттерна Strategy.
 */
public class UnitMenuMap {
    private static final Map<Integer, Unit> unitMap = new HashMap<>();

    static {
        try {
            unitMap.put(1, new Unit(UnitTypes.SWORDSMAN));
            unitMap.put(2, new Unit(UnitTypes.BOWMAN));
            unitMap.put(3, new Unit(UnitTypes.MAGE));
            unitMap.put(4, new Unit(UnitTypes.HEALER));
        } catch (UnitException e) {
            e.printStackTrace();
        }
    }

    public static Unit getDrawer(final Integer i) {
        return unitMap.get(i);
    }


}

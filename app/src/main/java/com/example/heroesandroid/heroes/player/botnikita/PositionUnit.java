package com.example.heroesandroid.heroes.player.botnikita;

import com.example.heroesandroid.heroes.mathutils.Position;
import com.example.heroesandroid.heroes.units.Unit;

public class PositionUnit {
    final private Position position;
    final private Unit unit;

    public PositionUnit(Position position, Unit unit) {
        this.position = position;
        this.unit = unit;
    }

    public Position getPosition() {
        return position;
    }

    public Unit getUnit() {
        return unit;
    }
}

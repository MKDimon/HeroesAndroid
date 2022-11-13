package com.example.heroesandroid.heroes.player.botgleb.clustering.clusteringfactory;

import com.example.heroesandroid.heroes.player.botgleb.clustering.UnitDecomposition;
import com.example.heroesandroid.heroes.units.Unit;

public class HealerClustering extends UnitClustering {

    public HealerClustering(final Unit unit, final int x, final int y) {
        super(unit, x, y);
    }

    @Override
    public double getUnitsValue() {
        if (y == 0) {
            return 0.5;
        }
        return 1d;
    }

    @Override
    public UnitDecomposition getUnitDecomposition() {
        final double value = getUnitsValue();
        return new UnitDecomposition(value * unit.getMaxHP(), value * unit.getArmor(),
                0, 0,
                0, (double)unit.getAccuracy()/100 * unit.getPower() * value);
    }
}

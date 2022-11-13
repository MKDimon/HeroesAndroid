package com.example.heroesandroid.heroes.boardfactory;

import com.example.heroesandroid.heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


public class DamageCommand extends Command {
    private static final Logger logger = LoggerFactory.getLogger(DamageCommand.class);

    public DamageCommand(final Unit att, final List<Unit> def) {
        super(att, def);

    }

    boolean getHitChance(final int accuracy) {
        Random r = new Random();
        final double rand = r.nextDouble();
        final double acc = accuracy / 100.0;
        logger.info("Unit accuracy {}", acc);
        logger.info("Randomly generated number {}", rand);
        //TODO: ternary operator
        return rand < acc;
    }

    int reducedDamage(final int pow, final int arm) {
        final double double_arm = (double) (arm) / 100.0;
        return pow - (int) ((double) pow * double_arm);
    }

    @Override
    public void execute() {
        for (Unit elem : super.getDef()) {
            if (getHitChance(super.getAtt().getAccuracy())) {
                elem.setCurrentHP(elem.getCurrentHP() -
                        reducedDamage(super.getAtt().getPower(), elem.getArmor()));
                logger.info("Unit hit! Dealing damage.");
                logger.info("Attacker power: {}. Reduced by armor damage = {}", super.getAtt().getPower(),
                        reducedDamage(super.getAtt().getPower(), elem.getArmor()));
                logger.info("Defender current hp: {}.", elem.getCurrentHP());
                logger.info("Defender max hp: {}.", elem.getMaxHP());
            } else {
                logger.info("Unit missed");
            }
        }
    }
}

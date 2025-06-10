package dnd.game.units;

import dnd.game.tiles.Unit;
import dnd.game.utils.Dice;
import dnd.game.utils.Position;

import java.util.List;

public class Warrior extends Player {
    private final int abilityCooldown;
    private int remainingCooldown;

    public Warrior(Position position, String name, int maxHealth, int attackPoints, int defensePoints,
                   int abilityCooldown) {
        super('@', position, name, maxHealth, attackPoints, defensePoints);
        this.abilityCooldown = abilityCooldown;
        remainingCooldown = 0;
    }

    @Override
    public void castAbility(List<Unit> unitList) {
        if (remainingCooldown > 0)
            notifyFailure("Cannot cast ability - ability is on cooldown");
        remainingCooldown = abilityCooldown;
        health.heal(10 * defensePoints);
        List<Unit> enemiesInRange = unitList.stream()
                .filter(unit -> position.distance(unit.getPosition()) < 3).toList();
        if (!enemiesInRange.isEmpty()) {
            Unit randomEnemy = Dice.chooseRandom(enemiesInRange);
            int damage = (int) (0.1 * health.getMaxHealth());
            randomEnemy.takeDamage(damage);
            notifyCast(this, randomEnemy, attackPoints, 0, damage, "Avenger’s Shield");
        } else notifyFailure("Cannot cast ability - no enemies in  range");
    }

    @Override
    public void onGameTick() {
        if (remainingCooldown > 0)
            remainingCooldown--;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        health.increaseMaxHealth(5 * level);
        attackPoints += 2 * level;
        defensePoints += level;
        health.setToMaxHealth();
        remainingCooldown = 0;
    }

    @Override
    public String description() {
        return super.description() + "\tCooldown: " + remainingCooldown + "/" + abilityCooldown;
    }
}

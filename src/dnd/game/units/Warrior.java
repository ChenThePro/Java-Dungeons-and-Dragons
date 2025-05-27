package dnd.game.units;

import dnd.game.tiles.*;

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
    public void castAbility() {
        if (remainingCooldown > 0)
            throw new IllegalStateException("Cannot cast ability - ability is on cooldown");
        remainingCooldown = abilityCooldown;
        int heal = 10 * defensePoints;
        health.heal(heal);
        // logic to hit random enemy by 10% of maxHealth within range < 3 deferred to controller
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

    @Override
    public void accept(Unit unit) {
        unit.visit(this);  // Generic visit back
    }

    @Override
    public void visit(Enemy enemy) {
        enemy.accept(this);
    }
}

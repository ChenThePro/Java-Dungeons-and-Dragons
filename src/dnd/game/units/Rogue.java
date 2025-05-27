package dnd.game.units;

import dnd.game.tiles.*;

public class Rogue extends Player {
    private final int cost;
    private int energy;

    public Rogue(Position position, String name, int maxHealth, int attackPoints, int defensePoints, int cost) {
        super('@', position, name, maxHealth, attackPoints, defensePoints);
        this.cost = cost;
        energy = 100;
    }

    @Override
    public void castAbility() {
        if (energy < cost)
            throw new IllegalStateException("Cannot cast ability - not enough energy");
        // For each enemy within range < 2, deal damage (reduce health value) equals to the rogue’s
        // attack points (each enemy will try to defend itself)
        energy -= cost;
        // logic deferred to controller/engine
    }

    @Override
    public void onGameTick() {
        energy = Math.min(energy + 10, 100);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        attackPoints += 3 * level;
        energy = 100;
    }

    @Override
    public String description() {
        return super.description() + "\tEnergy: " + energy;
    }

    @Override
    public void accept(Unit unit) {
        // TODO
    }

    @Override
    public void visit(Enemy enemy) {
        enemy.accept(this);
    }
}

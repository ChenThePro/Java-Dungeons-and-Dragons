package dnd.game.units;

import dnd.game.tiles.Unit;
import dnd.game.utils.Dice;
import dnd.game.utils.Position;
import dnd.game.utils.Resources;

import java.util.List;

public class Rogue extends Player {
    private final Resources resources;

    public Rogue(Position position, String name, int maxHealth, int attackPoints, int defensePoints, int cost) {
        super('@', position, name, maxHealth, attackPoints, defensePoints);
        resources = new Resources(100, cost);
    }

    @Override
    public void castAbility(List<Unit> unitList) {
        resources.consume();
        List<Unit> enemiesInRange = unitList.stream()
                .filter(unit -> position.distance(unit.getPosition()) < 2)
                .toList();
        if (enemiesInRange.isEmpty())
            notifyFailure("Cannot cast ability - no enemies in  range");
        else for (Unit enemy : enemiesInRange) {
            int defenseRoll = Dice.roll(enemy.getDefense());
            int damage = Math.max(0, attackPoints - defenseRoll);
            enemy.takeDamage(damage);
            notifyCast(this, enemy, attackPoints, defenseRoll, damage, "Fan of Knives");
        }
    }

    @Override
    public void onGameTick() {
        resources.regenerate(10);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        attackPoints += 3 * level;
        resources.regenerate(100);
        health.setToMaxHealth();
    }

    @Override
    public String description() {
        return super.description() + "\tEnergy: " + resources.getMaxResources();
    }
}

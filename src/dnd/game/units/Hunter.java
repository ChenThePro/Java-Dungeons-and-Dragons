package dnd.game.units;

import dnd.game.tiles.Unit;
import dnd.game.utils.Dice;
import dnd.game.utils.Position;

import java.util.List;

public class Hunter extends Player {
    private final int range;
    private int arrowsCount, ticksCount;

    public Hunter(Position position, String name, int maxHealth, int attackPoints, int defensePoints, int range) {
        super('@', position, name, maxHealth, attackPoints, defensePoints);
        this.range = range;
        arrowsCount = 10 * level;
        ticksCount = 0;
    }

    @Override
    public void castAbility(List<Unit> unitList) {
        if (arrowsCount == 0)
            notifyFailure("Cannot cast ability - no arrows");
        Unit closestEnemy = null;
        double closestDistance = Double.MAX_VALUE;
        double distance;
        for (Unit unit : unitList) {
            distance = position.distance(unit.getPosition());
            if (distance <= range && distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = unit;
            }
        }
        if (closestEnemy != null) {
            arrowsCount--;
            int defenseRoll = Dice.roll(closestEnemy.getDefense());
            int damage = Math.max(0, attackPoints - defenseRoll);
            closestEnemy.takeDamage(damage);
            notifyCast(this, closestEnemy, attackPoints, defenseRoll, damage, "Shoot");
        }
        else notifyFailure("Cannot cast ability - no enemies in  range");
    }

    @Override
    public void onGameTick() {
        if (ticksCount == 10) {
            arrowsCount += level;
            ticksCount = 0;
        }
        else ticksCount++;
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        arrowsCount += 10 * level;
        attackPoints += 2 * level;
        defensePoints += level;
        health.setToMaxHealth();
    }

    @Override
    public String description() {
        return super.description() + "\tArrows: " + arrowsCount;
    }
}

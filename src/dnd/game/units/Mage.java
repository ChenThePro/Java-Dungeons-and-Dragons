package dnd.game.units;

import dnd.game.tiles.Unit;
import dnd.game.utils.Dice;
import dnd.game.utils.Position;
import dnd.game.utils.Resources;

import java.util.ArrayList;
import java.util.List;

public class Mage extends Player {
    private int spellPower;
    private final int hitsCount;
    private final int abilityRange;
    private final Resources resources;

    public Mage(Position pos, String name, int maxHealth, int attackPoints, int defensePoints, int maxMana,
                int manaCost, int spellPower, int hitsCount, int abilityRange) {
        super('@', pos, name, maxHealth, attackPoints, defensePoints);
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
        resources = new Resources(maxMana, manaCost);
    }

    @Override
    public void castAbility(List<Unit> unitList) {
        resources.consume();
        List<Unit> enemiesInRange = new ArrayList<>(unitList.stream()
                .filter(unit -> position.distance(unit.getPosition()) < abilityRange)
                .toList());
        if (enemiesInRange.isEmpty())
            notifyFailure("Cannot cast ability - no enemies in  range");
        else {
            int hits = 0;
            while (hits < hitsCount && !enemiesInRange.isEmpty()) {
                Unit randomEnemy = Dice.chooseRandom(enemiesInRange);
                int defenseRoll = Dice.roll(randomEnemy.getDefense());
                int damage = Math.max(0, spellPower - defenseRoll);
                randomEnemy.takeDamage(damage);
                notifyCast(this, randomEnemy, spellPower, defenseRoll, damage, "Blizzard");
                if (randomEnemy.isDead())
                    enemiesInRange.remove(randomEnemy);
                hits++;
            }
        }
    }

    @Override
    public void onGameTick() {
        resources.regenerate(level);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        resources.increaseMaxResources(25 * level);
        spellPower += 10 * level;
        resources.regenerate(resources.getMaxResources() / 4);
        health.setToMaxHealth();
    }

    @Override
    public String description() {
        return super.description() + "\tMana: " + resources.getCurrentResources() + "/" + resources.getMaxResources() + "\tSpellPower: " + spellPower;
    }
}
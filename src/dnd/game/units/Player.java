package dnd.game.units;

import dnd.game.tiles.Unit;
import dnd.game.utils.Position;

public abstract class Player extends Unit implements HeroicUnit {
    protected int experience;
    protected int level;

    public Player(char tile, Position position, String name, int maxHealth, int attackPoints,
                  int defensePoints) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints);
        experience = 0;
        level = 1;
    }

    @Override
    public void accept(Unit unit) {
        super.accept(unit);
        if (isDead()) Die();
    }

    public void gainXP(int xp) {
        experience += xp;
        while (experience >= 50 * level) {
            levelUp();
            listener.onLevelUp(this);
        }
    }

    private void Die() {
        tile = 'X';
    }

    protected void levelUp() {
        experience -= 50 * level;
        level++;
        health.increaseMaxHealth(10 * level);
        attackPoints += 4 * level;
        defensePoints += level;
    }

    @Override
    public String description() {
        return super.description() + "\tLevel: " + level + "\tXP: " + experience;
    }

    public int getLevel() {
        return level;
    }
}

package dnd.game.units;

import dnd.game.tiles.Tile;
import dnd.game.tiles.Unit;
import dnd.game.tiles.Position;

public abstract class Player extends Unit implements HeroicUnit {
    protected int experience;
    protected int level;

    public Player(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints);
        experience = 0;
        level = 1;
    }

    public void gainXP(int xp) {
        experience += xp;
        if (experience >= 50 * level)
            levelUp();
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

    public abstract void interact(Tile tile); // override for visitor pattern
}

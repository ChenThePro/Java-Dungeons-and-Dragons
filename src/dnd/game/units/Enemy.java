package dnd.game.units;

import dnd.game.tiles.Position;
import dnd.game.tiles.Tile;
import dnd.game.tiles.Unit;

public abstract class Enemy extends Unit {
    protected int experienceValue;

    public Enemy(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints, int xp) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints);
        this.experienceValue = xp;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public String description() {
        return super.description() + "\tXP: " + experienceValue;
    }

    @Override
    public void interact(Tile tile) {
        tile.accept(this); // Allow the tile to control the visitor pattern behavior
    }

    @Override
    public void onGameTick() {
        // Default enemy does nothing. Override in subclasses (Monster, Trap, Boss)
    }
}

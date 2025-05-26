package dnd.game.units;

import dnd.game.tiles.Position;
import dnd.game.tiles.Unit;

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
    public void castAbility() {
        if (arrowsCount <= 0)
            throw new IllegalStateException("Cannot cast ability - no arrows");

        // logic to find closest enemy within range
        arrowsCount--;
    }

    @Override
    public void onGameTick() {
        ticksCount++;
        if (ticksCount == 10) {
            arrowsCount += level;
            ticksCount = 0;
        }
    }

    @Override
    protected void levelUp() {
        while (experience >= level * 50) {
            super.levelUp();
            arrowsCount += 10 * level;
            attackPoints += 2 * level;
            defensePoints += level;
        }
    }

    @Override
    public String description() {
        return super.description() + "\tArrows: " + arrowsCount;
    }

    @Override
    public void interact(dnd.game.tiles.Tile tile) {
        tile.accept(this);
    }

    @Override
    public void accept(Unit unit) {
        // TODO
    }
}

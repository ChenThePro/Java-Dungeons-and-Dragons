package dnd.game.units;

import dnd.game.tiles.Position;
import dnd.game.tiles.Unit;

public class Monster extends Enemy {
    protected int visionRange;

    public Monster(Position position, char tile, String name, int maxHealth, int attackPoints, int defensePoints,
                   int visionRange, int xp) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints, xp);
        this.visionRange = visionRange;
    }

    public int getVisionRange() {
        return visionRange;
    }

    @Override
    public void onGameTick() {
        // move toward player if in vision range
    }

    @Override
    public void accept(Unit unit) {
        // TODO
    }
}

package dnd.game.units;

import dnd.game.tiles.Position;
import dnd.game.tiles.Unit;
import dnd.game.utils.Dice;

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
        if (isDead()) return;
        Position playerPos = player.getPosition();
        double dist = position.distance(playerPos);
        if (dist < visionRange) {
            if (dist == 1)
                interact();
            else {
                int dx = playerPos.x() - position.x();
                int dy = playerPos.y() - position.y();
                if (Math.abs(dx) > Math.abs(dy))
                    tryMove(dx > 0 ? Direction.RIGHT : Direction.LEFT);
                else tryMove(dy > 0 ? Direction.DOWN : Direction.UP);
            }
        } else moveRandomly();
    }

    protected void moveRandomly() {
        Direction[] dirs = Direction.values();
        Direction d = dirs[Dice.roll(dirs.length - 1)];
        tryMove(d);
    }

    @Override
    public void accept(Unit unit) {
        // TODO
    }
}

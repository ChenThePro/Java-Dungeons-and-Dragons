package dnd.game.units;

import dnd.game.utils.Position;
import dnd.game.utils.Dice;
import dnd.game.utils.Direction;

public class Monster extends Enemy {
    protected final int visionRange;

    public Monster(Position position, char tile, String name, int maxHealth, int attackPoints, int defensePoints,
                   int visionRange, int xp) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints, xp);
        this.visionRange = visionRange;
    }

    @Override
    public void onGameTick() {
        Position playerPos = player.getPosition();
        double dist = position.distance(playerPos);
        if (dist < visionRange)
            chasePlayer(playerPos);
        else moveRandomly();
    }

    protected void chasePlayer(Position playerPos) {
        int dx = position.x() - playerPos.x();
        int dy = position.y() - playerPos.y();
        if (Math.abs(dx) > Math.abs(dy))
            tryMove(dx > 0 ? Direction.LEFT : Direction.RIGHT);
        else tryMove(dy > 0 ? Direction.UP : Direction.DOWN);
    }

    protected void moveRandomly() {
        Direction[] dirs = Direction.values();
        Direction d = dirs[Dice.roll(dirs.length - 1)];
        tryMove(d);
    }
}

package dnd.game.units;

import dnd.game.utils.Position;

public class Trap extends Enemy {
    private final int visibilityTime;
    private final int invisibilityTime;
    private int ticksCount;
    private boolean visible;

    public Trap(Position position, char tile, String name, int maxHealth, int attackPoints, int defensePoints, int xp,
                int visibilityTime, int invisibilityTime) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints, xp);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        ticksCount = 0;
        visible = true;
    }

    @Override
    public void onGameTick() {
        visible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime)) ticksCount = 0;
        else ticksCount++;
        if (position.distance(player.getPosition()) < 2)
            player.accept(this);
    }

    @Override
    public String toString() {
        return visible ? Character.toString(tile) : ".";
    }
}

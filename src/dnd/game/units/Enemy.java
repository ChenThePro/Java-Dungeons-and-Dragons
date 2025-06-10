package dnd.game.units;

import dnd.game.tiles.Empty;
import dnd.game.utils.Position;
import dnd.game.tiles.Tile;
import dnd.game.tiles.Unit;
import dnd.game.utils.Direction;

public abstract class Enemy extends Unit {
    protected int experienceValue;

    public Enemy(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints, int xp) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints);
        this.experienceValue = xp;
    }

    @Override
    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public String description() {
        return super.description() + "\tXP: " + experienceValue;
    }

    protected void tryMove(Direction dir) {
        int newX = position.x() + dir.dx;
        int newY = position.y() + dir.dy;
        Tile destination = board[newY][newX];
        destination.accept(this);
    }

    @Override
    public void accept(Unit unit) {
        if (unit.getExperienceValue() == 0) {
            super.accept(unit);
            if (isDead()) {
                unit.moveTo(this);
                board[position.y()][position.x()] = new Empty(position);
            }
        }
    }
}

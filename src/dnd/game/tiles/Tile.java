package dnd.game.tiles;

import dnd.game.units.Player;

import dnd.game.units.Enemy;

public abstract class Tile {
    protected char tile;
    protected Position position;

    public Tile(char tile, Position position) {
        this.tile = tile;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public char getTile() {
        return tile;
    }

    public abstract void accept(Unit unit); // Visitor pattern

    public abstract void visit(Player player);

    public abstract void visit(Enemy enemy);

    @Override
    public String toString() { return Character.toString(tile); }
}

package dnd.game.tiles;

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

    @Override
    public String toString() { return Character.toString(tile); }
}

package dnd.game.tiles;

public record Position(int x, int y) {

    public double distance(Position other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }
}

package dnd.game.tiles;

public class Wall extends Tile {

    public Wall(Position position) {
        super('#', position);
    }

    @Override
    public void accept(Unit unit) {
        // Do nothing — wall blocks movement
    }
}

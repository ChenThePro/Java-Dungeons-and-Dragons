package dnd.game.tiles;

import dnd.game.utils.Position;

public class Wall extends Tile {

    public Wall(Position position) {
        super('#', position);
    }

    @Override
    public void accept(Unit unit) {}
}

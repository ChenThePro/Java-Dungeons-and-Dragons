package dnd.game.tiles;

import dnd.game.utils.Position;

public class Empty extends Tile {

    public Empty(Position position) {
        super('.', position);
    }

    @Override
    public void accept(Unit unit) {
        unit.moveTo(this);
    }
}

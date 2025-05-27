package dnd.game.tiles;

import dnd.game.units.Enemy;
import dnd.game.units.Player;

public class Wall extends Tile {

    public Wall(Position position) {
        super('#', position);
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    @Override
    public void visit(Player player) {
        // Do nothing — wall blocks movement
    }

    @Override
    public void visit(Enemy enemy) {
        // Do nothing — wall blocks movement
    }


}

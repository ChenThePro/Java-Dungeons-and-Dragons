package dnd.game.tiles;

import dnd.game.units.Enemy;
import dnd.game.units.Player;

public class Empty extends Tile {

    public Empty(Position position) {
        super('.', position);
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    @Override
    public void visit(Player player) {
        player.move(this);  // Move to empty
    }

    @Override
    public void visit(Enemy enemy) {
        enemy.move(this);
    }

}

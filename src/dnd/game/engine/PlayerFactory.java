package dnd.game.engine;

import dnd.game.tiles.Position;
import dnd.game.units.Player;
import dnd.game.units.Warrior;

public class PlayerFactory {
    public static Player getDefaultPlayer(Position pos) {
        return new Warrior(pos, "Jon Snow", 300, 30, 4, 3);
    }
}


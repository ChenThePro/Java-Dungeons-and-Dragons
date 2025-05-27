package dnd.game.engine;

import dnd.game.units.Boss;
import dnd.game.units.Enemy;
import dnd.game.tiles.Position;
import dnd.game.units.Monster;
import dnd.game.units.Trap;

public class EnemyFactory {
    public static Enemy fromChar(char c, Position pos) {
        return switch (c) {
            case 's' -> new Monster(pos, c, "Lannister Soldier", 80, 8, 3, 3, 25);
            case 'B' -> new Trap(pos, c, "Bonus Trap", 1, 1, 1, 250, 1, 5);
            case 'M' -> new Boss(pos, c, "The Mountain", 1000, 60, 25, 6, 500, 5);
            // Add all cases...
            default -> throw new IllegalArgumentException("Unknown enemy tile: " + c);
        };
    }
}


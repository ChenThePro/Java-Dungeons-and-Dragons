package dnd.game.engine;

import dnd.game.units.Boss;
import dnd.game.units.Enemy;
import dnd.game.utils.Position;
import dnd.game.units.Monster;
import dnd.game.units.Trap;

public class EnemyFactory {
    public static Enemy fromChar(char c, Position pos) {
        return switch (c) {
            case 's' -> new Monster(pos, c, "Lannister Soldier", 80, 8, 3, 3, 25);
            case 'k' -> new Monster(pos, c, "Lannister Knight", 200, 14, 8, 4, 50);
            case 'q' -> new Monster(pos, c, "Queen's Guard", 400, 20, 15, 5, 100);
            case 'z' -> new Monster(pos, c, "Wright", 600, 30, 15, 3, 100);
            case 'b' -> new Monster(pos, c, "Bear-Wright", 1000, 75, 30, 4, 250);
            case 'g' -> new Monster(pos, c, "Giant-Wright", 1500, 100, 40, 5, 500);
            case 'w' -> new Monster(pos, c, "White Walker", 2000, 150, 50, 6, 1000);
            case 'M' -> new Boss(pos, c, "The Mountain", 1000, 60, 25, 6, 500, 5);
            case 'C' -> new Boss(pos, c, "Queen Cersei", 100, 10, 10, 1, 1000, 8);
            case 'K' -> new Boss(pos, c, "Night's King", 5000, 300, 150, 8, 5000, 3);
            case 'B' -> new Trap(pos, c, "Bonus Trap", 1, 1, 1, 250, 1, 5);
            case 'Q' -> new Trap(pos, c, "Queen's Trap", 250, 50, 10, 100, 3, 7);
            case 'D' -> new Trap(pos, c, "Death Trap", 500, 100, 20, 250, 1, 10);
            default -> throw new IllegalArgumentException("Unknown enemy tile: " + c);
        };
    }
}

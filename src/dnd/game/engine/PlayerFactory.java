package dnd.game.engine;

import dnd.game.units.*;
import dnd.game.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class PlayerFactory {

    public static Player createPlayer(int playerType, Position pos) {
        return switch (playerType) {
            case 0 -> new Warrior(pos, "Jon Snow", 300, 30, 4, 3);
            case 1 -> new Warrior(pos, "The Hound", 400, 20, 6, 5);
            case 2 -> new Mage(pos, "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
            case 3 -> new Mage(pos, "Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4);
            case 4 -> new Rogue(pos, "Arya Stark", 150, 40, 2, 20);
            case 5 -> new Rogue(pos, "Bronn", 250, 35, 3, 50);
            case 6 -> new Hunter(pos, "Ygritte", 220, 30, 2, 6);
            default -> throw new IllegalArgumentException("Invalid player type: " + playerType);
        };
    }

    public static List<Player> listPlayer() {
        List<Player> players = new ArrayList<>();
        Position defaultPos = new Position(0, 0);
        for (int i = 0; i < 7; i++)
            players.add(createPlayer(i, defaultPos));
        return players;
    }
}

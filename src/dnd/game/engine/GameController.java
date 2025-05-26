package dnd.game.engine;

import dnd.game.tiles.*;
import dnd.game.units.*;

import java.util.*;

public class GameController {
    private Tile[][] board;
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();

    public GameController(Tile[][] board, Player player) {
        this.board = board;
        this.player = player;
    }

    public void gameTick(char input) {
        switch (input) {
            case 'w', 'a', 's', 'd' -> movePlayer(input);
            case 'e' -> player.castAbility();
            case 'q' -> {} // Do nothing
        }

        player.onGameTick();
        for (Enemy e : enemies) e.onGameTick();
        // Enemy movement + combat logic
    }

    private void movePlayer(char dir) {
        // Compute target Position, validate tile, call tile.accept(player)
    }

    public void printBoard() {
        for (Tile[] row : board) {
            for (Tile t : row)
                System.out.print(t.toString());
            System.out.println();
        }
    }

    public void displayStats() {
        System.out.println(player.description());
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }
}

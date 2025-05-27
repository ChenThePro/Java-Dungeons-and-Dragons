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
        player.setBoard(board);
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
        int dx = 0, dy = 0;
        switch (dir) {
            case 'w' -> dy = -1;
            case 's' -> dy = 1;
            case 'a' -> dx = -1;
            case 'd' -> dx = 1;
        }
        Position currentPos = player.getPosition();
        int newX = currentPos.x() + dx;
        int newY = currentPos.y() + dy;
        if (newX >= 0 && newX < board[0].length && newY >= 0 && newY < board.length) {
            Tile targetTile = board[newY][newX];
            targetTile.accept(player);
        }
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
        for (Enemy e : enemies) {
            e.setPlayerReference(player);
            e.setBoard(board);
        }
    }
}

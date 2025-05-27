package dnd.game.engine;

import dnd.game.tiles.*;
import dnd.game.units.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LevelManager {
    private final String levelsPath;
    private int currentLevel = 1;

    public LevelManager(String levelsPath) {
        this.levelsPath = levelsPath;
    }

    public LevelData loadNextLevel() throws IOException {
        String fileName = String.format("level_%d", currentLevel++);
        Path path = Paths.get(levelsPath, fileName);

        if (!Files.exists(path)) return null;

        List<String> lines = Files.readAllLines(path);
        int rows = lines.size(), cols = lines.get(0).length();
        Tile[][] board = new Tile[rows][cols];
        List<Enemy> enemies = new ArrayList<>();
        Player player = null;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                char c = lines.get(y).charAt(x);
                Position pos = new Position(x, y);

                Tile t;
                switch (c) {
                    case '#' -> t = new Wall(pos);
                    case '.' -> t = new Empty(pos);
                    case '@' -> {
                        player = PlayerFactory.getDefaultPlayer(pos); // Replace with menu choice
                        t = player;
                    }
                    default -> {
                        Enemy e = EnemyFactory.fromChar(c, pos);
                        enemies.add(e);
                        t = e;
                    }
                }
                board[y][x] = t;
            }
        }

        return new LevelData(board, player, enemies);
    }

    public record LevelData(Tile[][] board, Player player, List<Enemy> enemies) {}
}

package dnd.game.engine;

import dnd.game.tiles.*;
import dnd.game.units.*;
import dnd.game.utils.Position;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LevelManager {
    private final String levelsPath;
    private int currentLevel = 1;

    public LevelManager(String levelsPath) {
        this.levelsPath = levelsPath;
    }

    public LevelData loadNextLevel(Player player) throws IOException {
        String fileName = String.format("level%d", currentLevel++);
        Path path = Paths.get(levelsPath, fileName);
        if (!Files.exists(path)) return null;
        List<String> lines = Files.readAllLines(path);
        int rows = lines.size(), cols = lines.getFirst().length();
        Tile[][] board = new Tile[rows][cols];
        List<Unit> enemies = new ArrayList<>();
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < cols; x++) {
                char c = lines.get(y).charAt(x);
                Position pos = new Position(x, y);
                Tile t;
                switch (c) {
                    case '#' -> t = new Wall(pos);
                    case '.' -> t = new Empty(pos);
                    case '@' -> {
                        player.setPosition(pos);
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

        return new LevelData(board, player, enemies);
    }

    public record LevelData(Tile[][] board, Player player, List<Unit> enemies) {}
}

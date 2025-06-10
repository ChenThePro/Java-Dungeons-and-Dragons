package dnd.game.engine;

import dnd.game.tiles.*;
import dnd.game.units.Player;
import dnd.game.tiles.Unit;
import dnd.game.utils.Direction;
import dnd.game.utils.Position;

import java.util.*;

public class GameController {
    private final Tile[][] board;
    private final Player player;
    private List<Unit> enemies = new ArrayList<>();
    private GameEventListener listener;

    public GameController(Tile[][] board, Player player) {
        this.board = board;
        this.player = player;
        player.setBoard(board);
    }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
        player.setEventListener(listener);
        for (Unit e : enemies)
            e.setEventListener(listener);
    }

    public void setEnemies(List<Unit> enemies) {
        this.enemies = enemies;
        for (Unit e : enemies) {
            e.setPlayerReference(player);
            e.setBoard(board);
        }
    }

    public void gameTick(char input) {
        input = Character.toLowerCase(input);
        switch (input) {
            case 'w', 'a', 's', 'd' -> movePlayer(input);
            case 'e' -> castAbility();
            case 'q' -> {}
            default -> listener.onFailure("Invalid input. Use w/a/s/d for movement, e for ability, q to skip.");
        }
        player.onGameTick();
        enemies.forEach(Unit::onGameTick);
    }

    private void castAbility() {
        player.castAbility(enemies);
        List<Unit> deadEnemies = new ArrayList<>();
        for (Unit unit : enemies)
            if (unit.isDead()) {
                deadEnemies.add(unit);
                player.gainXP(unit.getExperienceValue());
                board[unit.getPosition().y()][unit.getPosition().x()] = new Empty(unit.getPosition());
            }
        enemies.removeAll(deadEnemies);
    }

    private void movePlayer(char dir) {
        int dx = 0, dy = 0;
        switch (dir) {
            case 'w' -> dy = Direction.UP.dy;
            case 's' -> dy = Direction.DOWN.dy;
            case 'a' -> dx = Direction.LEFT.dx;
            case 'd' -> dx = Direction.RIGHT.dx;
        }
        Position curr = player.getPosition();
        int newX = curr.x() + dx;
        int newY = curr.y() + dy;
        if (newX >= 0 && newX < board[0].length && newY >= 0 && newY < board.length)
            board[newY][newX].accept(player);
        Iterator<Unit> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Unit enemy = iterator.next();
            if (enemy.isDead()) {
                iterator.remove();
                return;
            }
        }
    }

    public void printBoard() {
        for (Tile[] row : board) {
            for (Tile t : row) System.out.print(t);
            System.out.println();
        }
    }

    public void displayStats() {
        System.out.println(player.description());
    }
}
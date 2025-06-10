package dnd;

import dnd.game.engine.*;
import dnd.game.units.Player;
import dnd.game.utils.Position;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Error: this program needs a path to the levels directory as an argument.");
            System.exit(1);
        }
        String levelDir = args[0];
        LevelManager levelManager = new LevelManager(levelDir);
        Player player = PlayerFactory.createPlayer(CLI.choosePlayer(), new Position(0, 0));
        LevelManager.LevelData level;
        GameController controller;
        CLI ui;
        while ((level = levelManager.loadNextLevel(player)) != null) {
            controller = new GameController(level.board(), level.player());
            controller.setEnemies(level.enemies());
            ui = new CLI(controller);
            while (!level.player().isDead() && !level.enemies().isEmpty())
                ui.start();
            if (level.player().isDead()) {
                controller.printBoard();
                System.out.println("Game Over \uD83D\uDC80");
                System.exit(-1);
            }
        }
        System.out.println("\uD83C\uDFC6 You Win!");
    }
}
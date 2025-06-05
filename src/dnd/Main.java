package dnd;

import dnd.game.tiles.*;
import dnd.game.engine.*;
import dnd.game.units.Player;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Error: this program needs a path to the levels directory as an argument.");
            System.exit(-1);
        }
        String levelDir = args[0];
        LevelManager lm = new LevelManager(levelDir);
        LevelManager.LevelData level;
        Player player = PlayerFactory.createPlayer(CLI.choosePlayer(), new Position(0, 0));
        while ((level = lm.loadNextLevel(player)) != null) {
            GameController controller = new GameController(level.board(), level.player());
            controller.setEnemies(level.enemies());
            Scanner scanner = new Scanner(System.in);
            while (!level.player().isDead() && !level.enemies().isEmpty()) {
                controller.printBoard();
                controller.displayStats();
                char input = scanner.nextLine().charAt(0);
                controller.gameTick(input);
                level.enemies().removeIf(Unit::isDead);
            }
            if (level.player().isDead()) {
                controller.printBoard();
                System.out.println("Game Over 💀");
                System.exit(-1);
            }
        }
        System.out.println("🏆 You Win!");
        // GameController controller = new GameController(board, player)
        // CLI cli = new CLI(controller)
        // cli.startGame()
        // GameRunner gameRunner = new GameRunner()
        // gameRunner.Initialize(args[0])
        // gameRunner.start()
    }
}

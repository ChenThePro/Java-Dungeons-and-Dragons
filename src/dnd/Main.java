package dnd;

import dnd.game.tiles.*;
import dnd.game.units.*;
import dnd.game.engine.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: this program needs a path to the levels directory as an argument.");
            System.exit(-1);
        }
        // Placeholder board (10x10), load real level from file
        Tile[][] board = new Tile[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                board[i][j] = new Empty(new Position(j, i));

        Player player = new Rogue(new Position(1, 1), "Arya Stark", 150, 40, 2, 20);
        board[1][1] = player;

        GameController controller = new GameController(board, player);
        CLI cli = new CLI(controller);
        cli.startGame();
        // GameRunner gameRunner = new GameRunner()
        // gameRunner.Initialize(args[0])
        // gameRunner.start()
    }
}

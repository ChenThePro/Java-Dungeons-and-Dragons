package dnd.game.engine;

import java.util.Scanner;

public class CLI {
    private final Scanner scanner = new Scanner(System.in);
    private final GameController controller;

    public CLI(GameController controller) {
        this.controller = controller;
    }

    public void startGame() {
        while (true) {
            controller.printBoard();
            controller.displayStats();
            System.out.print("Action (w/a/s/d/e/q): ");
            char input = scanner.nextLine().charAt(0);
            controller.gameTick(input);
        }
    }
}

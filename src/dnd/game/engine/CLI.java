package dnd.game.engine;

import dnd.game.tiles.Unit;
import dnd.game.units.Player;

import java.util.List;
import java.util.Scanner;

public class CLI {
    private static final Scanner scanner = new Scanner(System.in);
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

    public static void announceLevelUp(Player p) {
        System.out.println("🎉 " + p.getName() + " leveled up to " + p.getLevel());
        System.out.println(p.description());
    }

    public static void reportBossCast(Unit boss, Player target, int atk, int def, int dmg) {
        System.out.println("🔥 " + boss.getName() + " casts Shoebodybop on " + target.getName());
        System.out.println("Attack: " + atk + ", Defense: " + def + ", Damage: " + dmg);
    }

    public static void reportCombat(Unit attacker, Unit defender, int atk, int def, int dmg) {
        System.out.println(attacker.getName() + " engages " + defender.getName());
        System.out.println("Attack Roll: " + atk + ", Defense Roll: " + def);
        System.out.println("Damage dealt: " + dmg);
        System.out.println("defender has " + defender.getHealth() + " health");
        if (defender.isDead())
            System.out.println(defender.getName() + " died.");
    }

    public static int choosePlayer() {
        while (true) {
            System.out.println("Select player");
            List<Player> playerList = PlayerFactory.listPlayer();
            for (int i = 0; i < playerList.size(); i++)
                System.out.printf("%d. %s%n", i + 1, playerList.get(i).description());
            try {
                int selected = Integer.parseInt(scanner.next()) - 1;
                if (0 <= selected && selected < playerList.size()) {
                    System.out.printf("You have selected: \n%s\n", playerList.get(selected).getName());
                    return selected;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Not a number");
            }
        }
    }
}

package dnd.game.engine;

import dnd.game.tiles.Unit;
import dnd.game.units.Player;

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
        if (defender.isDead()) {
            System.out.println(defender.getName() + " died.");
        }
    }

}

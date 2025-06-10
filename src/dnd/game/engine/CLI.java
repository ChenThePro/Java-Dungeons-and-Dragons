package dnd.game.engine;

import dnd.game.tiles.Unit;
import dnd.game.units.Player;

import java.util.List;
import java.util.Scanner;

public class CLI implements GameEventListener {
    private final Scanner scanner;
    private final GameController controller;

    public CLI(GameController controller) {
        this.controller = controller;
        scanner = new Scanner(System.in);
        controller.setListener(this);
    }

    public void start() {
        controller.printBoard();
        controller.displayStats();
        System.out.print("Action (w/a/s/d/e/q): ");
        char input = scanner.nextLine().charAt(0);
        controller.gameTick(input);
    }

    public static int choosePlayer() {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = PlayerFactory.listPlayer();
        while (true) {
            System.out.println("Select player:");
            for (int i = 0; i < players.size(); i++)
                System.out.printf("%d. %s%n", i + 1, players.get(i).description());
            try {
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < players.size()) {
                    System.out.println("You selected: " + players.get(choice).getName());
                    return choice;
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    @Override
    public void onFailure(String message) {
        System.out.println("Error: " + message);
    }

    @Override
    public void onCombat(Unit attacker, Unit defender, int atk, int def, int dmg) {
        System.out.println(attacker.getName() + " engages " + defender.getName());
        System.out.println("Attack Roll: " + atk + ", Defense Roll: " + def);
        System.out.println("Damage dealt: " + dmg);
        System.out.println("defender has " + defender.getHealth() + " health");
        if (defender.isDead())
            System.out.println(defender.getName() + " died.");
    }

    @Override
    public void onCast(Unit caster, Unit target, int atk, int def, int dmg, String spell) {
        System.out.println("🔥 " + caster.getName() + " casts " + spell + " on " + target.getName());
        System.out.println("Attack: " + atk + ", Defense: " + def + ", Damage: " + dmg);
        System.out.println("defender has " + target.getHealth() + " health");
        if (target.isDead())
            System.out.println(target.getName() + " died.");
    }

    @Override
    public void onLevelUp(Player p) {
        System.out.println("🎉 " + p.getName() + " leveled up to " + p.getLevel());
        System.out.println(p.description());
    }
}
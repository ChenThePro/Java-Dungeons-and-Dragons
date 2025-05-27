package dnd.game.units;

import dnd.game.engine.CLI;
import dnd.game.tiles.Position;
import dnd.game.tiles.Tile;
import dnd.game.tiles.Unit;
import dnd.game.utils.Dice;

public abstract class Enemy extends Unit {
    protected int experienceValue;

    public Enemy(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints, int xp) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints);
        this.experienceValue = xp;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public String description() {
        return super.description() + "\tXP: " + experienceValue;
    }

    public void engage(Player player) {
        int attackRoll = Dice.roll(attackPoints);
        int defenseRoll = Dice.roll(player.getDefense());
        int damage = Math.max(attackRoll - defenseRoll, 0);

        CLI.reportCombat(this, player, attackRoll, defenseRoll, damage);

        player.takeDamage(damage);

        if (player.isDead()) {
            player.Die();
        }
    }

    public int getDefense() {
        return defensePoints;
    }

    protected void tryMove(Direction dir) {
        int newX = position.x() + dir.dx;
        int newY = position.y() + dir.dy;

        Tile destination = board[newY][newX];
        destination.accept(this); // Calls visitor logic
    }

    @Override
    public void onGameTick() {
        // Default enemy does nothing. Override in subclasses (Monster, Trap, Boss)
    }
}

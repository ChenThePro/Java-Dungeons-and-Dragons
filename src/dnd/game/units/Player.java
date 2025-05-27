package dnd.game.units;

import dnd.game.engine.CLI;
import dnd.game.tiles.Empty;
import dnd.game.tiles.Unit;
import dnd.game.tiles.Position;
import dnd.game.utils.Dice;

public abstract class Player extends Unit implements HeroicUnit {
    protected int experience;
    protected int level;

    public Player(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints) {
        super(tile, position, name, maxHealth, attackPoints, defensePoints);
        experience = 0;
        level = 1;
    }

    public void gainXP(int xp) {
        experience += xp;
        while (experience >= 50 * level) {
            experience -= 50 * level;
            levelUp();
        }
    }

    public void Die() {
        tile = 'X';
    }

    protected void levelUp() {
        experience -= 50 * level;
        level++;
        health.increaseMaxHealth(10 * level);
        attackPoints += 4 * level;
        defensePoints += level;
        CLI.announceLevelUp(this);
    }

    public int getDefense() {
        return defensePoints;
    }

    @Override
    public String description() {
        return super.description() + "\tLevel: " + level + "\tXP: " + experience;
    }

    public void engage(Enemy enemy) {
        int attackRoll = Dice.roll(attackPoints);
        int defenseRoll = Dice.roll(enemy.getDefense());
        int damage = Math.max(attackRoll - defenseRoll, 0);

        CLI.reportCombat(this, enemy, attackRoll, defenseRoll, damage);

        enemy.takeDamage(damage);

        if (enemy.isDead()) {
            gainXP(enemy.getExperienceValue());
            board[enemy.getPosition().x()][enemy.getPosition().y()] = board[getPosition().x()][getPosition().y()];
            board[getPosition().x()][getPosition().y()] = new Empty(enemy.getPosition());
        }
    }

    public int getLevel() {
        return level;
    }
}

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

    public void interact(Enemy enemy) {
        int attackRoll = Dice.roll(attackPoints);
        int defenseRoll = Dice.roll(enemy.getDefense());
        int damage = Math.max(attackRoll - defenseRoll, 0);
        enemy.takeDamage(damage);
        CLI.reportCombat(this, enemy, attackRoll, defenseRoll, damage);
        if (enemy.isDead()) {
            gainXP(enemy.getExperienceValue());
            board[enemy.getPosition().y()][enemy.getPosition().x()] = this;
            board[getPosition().y()][getPosition().x()] = new Empty(getPosition());
            setPosition(enemy.getPosition());
        }
    }

    public void gainXP(int xp) {
        experience += xp;
        while (experience >= 50 * level) {
            levelUp();
            CLI.announceLevelUp(this);
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
    }

    @Override
    public String description() {
        return super.description() + "\tLevel: " + level + "\tXP: " + experience;
    }

    public int getLevel() {
        return level;
    }
}

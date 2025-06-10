package dnd.game.tiles;

import dnd.game.engine.GameEventListener;
import dnd.game.units.Information;
import dnd.game.units.Player;
import dnd.game.utils.Dice;
import dnd.game.utils.Position;

public abstract class Unit extends Tile implements Information {
    protected String name;
    protected Health health;
    protected int attackPoints, defensePoints;
    protected Player player;
    protected Tile[][] board;
    protected GameEventListener listener;

    public Unit(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints) {
        super(tile, position);
        this.name = name;
        health = new Health(maxHealth);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    public int getExperienceValue() {
        return 0;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public void moveTo(Tile target) {
        Position current = position;
        Position targetPos = target.getPosition();
        board[current.y()][current.x()] = target;
        board[targetPos.y()][targetPos.x()] = this;
        position = targetPos;
        target.setPosition(current);
    }

    public int getDefense() {
        return defensePoints;
    }

    public int getAttack() {
        return attackPoints;
    }

    public void setEventListener(GameEventListener listener) {
        this.listener = listener;
    }

    protected void notifyFailure(String message) {
        listener.onFailure(message);
    }

    protected void notifyCombat(Unit attacker, Unit defender, int attackRoll, int defenseRoll, int damage) {
        listener.onCombat(attacker, defender, attackRoll, defenseRoll, damage);
    }

    protected void notifyCast(Unit attacker, Unit defender, int attackRoll, int defenseRoll, int damage, String spell) {
        listener.onCast(attacker, defender, attackRoll, defenseRoll, damage, spell);
    }

    protected static class Health {
        private int maxHealth;
        private int currentHealth;

        public Health(int maxHealth) {
            this.maxHealth = maxHealth;
            currentHealth = maxHealth;
        }

        public void takeDamage(int damage) {
            currentHealth = Math.max(currentHealth - damage, 0);
        }

        public void heal(int healAmount) {
            currentHealth = Math.min(currentHealth + healAmount, maxHealth);
        }

        public void increaseMaxHealth(int amount) {
            maxHealth += amount;
        }

        public void setToMaxHealth() {
            currentHealth = maxHealth;
        }

        public boolean isDead() {
            return currentHealth == 0;
        }

        public int getCurrentHealth() {
            return currentHealth;
        }

        public int getMaxHealth() {
            return maxHealth;
        }
    }

    public void setPlayerReference(Player p) {
        player = p;
    }

    public int getHealth() {
        return health.getCurrentHealth();
    }

    public abstract void onGameTick();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String description() {
        return String.format("%s\tHealth: %d/%d\tAttack: %d\tDefense: %d",
                name, health.getCurrentHealth(), health.getMaxHealth(), attackPoints, defensePoints);
    }

    public void takeDamage(int damage) {
        health.takeDamage(damage);
    }

    public boolean isDead() {
        return health.isDead();
    }

    @Override
    public void accept(Unit unit) {
        int attackRoll = Dice.roll(unit.getAttack());
        int defenseRoll = Dice.roll(defensePoints);
        int damage = Math.max(attackRoll - defenseRoll, 0);
        takeDamage(damage);
        notifyCombat(unit, this, attackRoll, defenseRoll, damage);
    }
}
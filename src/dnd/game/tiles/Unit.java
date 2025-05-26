package dnd.game.tiles;

import dnd.game.units.Information;

import java.util.Random;

public abstract class Unit extends Tile implements Information {
    protected String name;
    protected Health health;
    protected int attackPoints, defensePoints;

    public Unit(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints) {
        super(tile, position);
        this.name = name;
        health = new Health(maxHealth);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
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

    public void moveTo(Tile target) {
        this.position = target.getPosition();
    }

    public abstract void onGameTick();

    public abstract void interact(Tile t);

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

    public int rollAttack() {
        return new Random().nextInt(attackPoints + 1);
    }

    public int rollDefense() {
        return new Random().nextInt(defensePoints + 1);
    }
}
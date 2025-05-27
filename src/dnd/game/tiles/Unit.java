package dnd.game.tiles;

import dnd.game.units.Enemy;
import dnd.game.units.Information;
import dnd.game.units.Player;

public abstract class Unit extends Tile implements Information {
    protected String name;
    protected Health health;
    protected int attackPoints, defensePoints;
    protected Player player;
    protected Tile[][] board;

    public Unit(char tile, Position position, String name, int maxHealth, int attackPoints, int defensePoints) {
        super(tile, position);
        this.name = name;
        health = new Health(maxHealth);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    protected void move(Tile target) {
        Position current = this.position;
        Position targetPos = target.getPosition();

        board[current.y()][current.x()] = new Empty(current);
        board[targetPos.y()][targetPos.x()] = this;

        this.position = targetPos;
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

    // Injected into enemies
    public void setPlayerReference(Player p) {
        player = p;
    }


    public void visit(Empty tile) {
        move(tile);
    }

    @Override
    public void visit(Player player) {
        if (this instanceof Enemy) {
            player.engage((Enemy) this);
        }
    }


    public void visit(Wall wall) {
        // Blocked — do nothing
    }

    public void visit(Unit other) {
        if (this instanceof Player && other instanceof Enemy) {
            ((Player) this).engage((Enemy) other);
        } else if (this instanceof Enemy && other instanceof Player) {
            ((Enemy) this).engage((Player) other);
        }
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
}
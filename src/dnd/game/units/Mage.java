package dnd.game.units;

import dnd.game.tiles.Position;
import dnd.game.tiles.Unit;

public class Mage extends Player {
    protected Mana mana;
    private final int manaCost;
    private int spellPower;
    private int hitsCount;
    private int abilityRange;

    public Mage(Position pos, String name, int maxHealth, int attackPoints, int defensePoints, int maxMana,
                int manaCost, int spellPower, int hitsCount, int abilityRange) {
        super('@', pos, name, maxHealth, attackPoints, defensePoints);
        this.mana = new Mana(maxMana);
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    protected static class Mana {
        private int maxMana;
        private int currentMana;

        public Mana(int maxMana) {
            this.maxMana = maxMana;
            currentMana = maxMana / 4;
        }

        private boolean canCast(int cost) {
            return currentMana >= cost;
        }

        public void consume(int cost) {
            if (!canCast(cost))
                throw new IllegalStateException("Cannot cast ability - not enough mana");
            currentMana -= cost;
        }

        public void regenerate(int amount) {
            currentMana = Math.min(maxMana, currentMana + amount);
        }

        public void increaseMaxMana(int amount) {
            maxMana += amount;
        }

        public void restorePartial(int amount) {
            currentMana = Math.min(currentMana + amount, maxMana);
        }

        public int getCurrentMana() {
            return currentMana;
        }

        public int getMaxMana() {
            return maxMana;
        }
    }

    @Override
    public void castAbility() {
        mana.consume(manaCost);
        int hits = 0;
        while (hits < hitsCount) { // || exists enemy that position.distance(enemy) < abilityRange)
            // select random enemy within ability range
            // Deal damage (reduce health value) to the chosen enemy for an amount equal to spell power
            // (each enemy may try to defend itself).
            hits++;
        }
        // controller handles random selection within range < abilityRange in power spellPower
        // for up to hitsCount
    }

    @Override
    public void onGameTick() {
        mana.regenerate(level);
    }

    @Override
    public void levelUp() {
        while (experience >= level * 50) {
            super.levelUp();
            mana.increaseMaxMana(25 * level);
            spellPower += 10 * level;
        }
        mana.restorePartial(mana.getMaxMana() / 4);
    }

    @Override
    public String description() {
        return super.description() + "\tMana: " + mana.getCurrentMana() + "/" + mana.getMaxMana() + "\tSpellPower: " + spellPower;
    }

    @Override
    public void interact(dnd.game.tiles.Tile tile) {
        tile.accept(this);
    }

    @Override
    public void accept(Unit unit) {
        // TODO
    }
}
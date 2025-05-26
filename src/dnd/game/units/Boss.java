package dnd.game.units;

import dnd.game.tiles.Position;

public class Boss extends Monster implements HeroicUnit {
    private final int abilityFrequency;
    private int combatTicks;

    public Boss(Position position, char tile, String name, int maxHealth, int attackPoints, int defensePoints,
                int visionRange, int xp, int abilityFrequency) {
        super(position, tile, name, maxHealth, attackPoints, defensePoints, visionRange, xp);
        this.abilityFrequency = abilityFrequency;
        combatTicks = 0;
    }

    @Override
    public void onGameTick() {
        // move like monster, but:
        combatTicks++;
        if (combatTicks == abilityFrequency) {
            castAbility();
            combatTicks = 0;
        }
    }

    @Override
    public void castAbility() {
        // Shoebodybop: shoot player if in vision range
    }
}

package dnd.game.units;

import dnd.game.tiles.Unit;
import dnd.game.utils.Position;
import dnd.game.utils.Dice;

import java.util.List;

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
        Position playerPos = player.getPosition();
        double dist = position.distance(playerPos);
        if (dist < visionRange) {
            if (combatTicks == abilityFrequency) {
                combatTicks = 0;
                castAbility(null);
            }
            else {
                combatTicks++;
                chasePlayer(playerPos);
            }
        } else {
            combatTicks = 0;
            moveRandomly();
        }
    }

    @Override
    public void castAbility(List<Unit> unitList) {
        int defenseRoll = Dice.roll(player.getDefense());
        int dmg = Math.max(attackPoints - defenseRoll, 0);
        player.takeDamage(dmg);
        notifyCast(this, player, attackPoints, defenseRoll, dmg, "Shoebodybop");
    }
}

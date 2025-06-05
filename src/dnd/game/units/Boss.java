package dnd.game.units;

import dnd.game.engine.CLI;
import dnd.game.tiles.Position;
import dnd.game.utils.Dice;

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
        if (!isDead()) return;
        Position playerPos = player.getPosition();
        double dist = position.distance(playerPos);
        if (dist < visionRange) {
            if (combatTicks >= abilityFrequency) {
                combatTicks = 0;
                castAbility();  // Shoebodybop
            } else {
                combatTicks++;
            }
            chasePlayer();
        } else {
            combatTicks = 0;
            moveRandomly();
        }
        // move like monster, but:
       //  combatTicks++;
       // if (combatTicks == abilityFrequency) {
       //     castAbility();
       //     combatTicks = 0;
       // }
    }

    private void chasePlayer() {
        int dx = player.getPosition().x() - position.x();
        int dy = player.getPosition().y() - position.y();

        if (Math.abs(dx) > Math.abs(dy)) {
            tryMove(dx > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
            tryMove(dy > 0 ? Direction.DOWN : Direction.UP);
        }
    }

    @Override
    public void castAbility() {
        int attackRoll = Dice.roll(attackPoints);
        int defenseRoll = Dice.roll(player.getDefense());
        int dmg = Math.max(attackRoll - defenseRoll, 0);
        player.takeDamage(dmg);
        CLI.reportBossCast(this, player, attackRoll, defenseRoll, dmg);
    }
}

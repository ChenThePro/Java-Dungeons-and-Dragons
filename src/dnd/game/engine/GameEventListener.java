package dnd.game.engine;

import dnd.game.tiles.Unit;
import dnd.game.units.Player;

public interface GameEventListener {
    void onFailure(String message);
    void onCombat(Unit attacker, Unit defender, int atkRoll, int defRoll, int dmg);
    void onCast(Unit caster, Unit target, int atk, int def, int dmg, String spell);
    void onLevelUp(Player player);
}

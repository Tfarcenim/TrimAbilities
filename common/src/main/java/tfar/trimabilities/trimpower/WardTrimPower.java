package tfar.trimabilities.trimpower;

import net.minecraft.world.entity.player.Player;
import tfar.trimabilities.TrimTier;

import java.util.function.Consumer;

public class WardTrimPower extends TrimPower {
    public WardTrimPower(int cooldown, TrimTier tier, Consumer<Player> consumer) {
        super(cooldown, tier, null, consumer);
    }


    @Override
    public void applyPassiveEffects(Player player) {

    }
}

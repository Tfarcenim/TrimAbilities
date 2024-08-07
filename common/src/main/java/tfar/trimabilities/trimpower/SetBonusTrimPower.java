package tfar.trimabilities.trimpower;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import tfar.trimabilities.BooleanFunction;
import tfar.trimabilities.PlayerDuck;
import tfar.trimabilities.TrimPowers;
import tfar.trimabilities.TrimTier;

import java.util.function.Consumer;

public class SetBonusTrimPower extends TrimPower {
    private final Holder<MobEffect> setBonusEffect;

    public SetBonusTrimPower(int cooldown, TrimTier tier, BooleanFunction<Player> consumer, Holder<MobEffect> setBonusEffect) {
        super(cooldown, tier, null, consumer);
        this.setBonusEffect = setBonusEffect;
    }


    @Override
    public void applyPassiveEffects(Player player) {

    }


    @Override
    public void applySetBonus(Player player, int pieces) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        if (playerDuck.getTrimPower() > tier.passive) {
            if (pieces == 1) {
                player.addEffect(TrimPowers.createTempEffect(setBonusEffect, 200, 0));
            }
            if (pieces > 1) {
                player.addEffect(TrimPowers.createTempEffect(setBonusEffect, 200, 1));
            }
        }
    }
}

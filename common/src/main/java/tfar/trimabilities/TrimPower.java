package tfar.trimabilities;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TrimPower {
    protected final int cooldown;
    private final TrimTier tier;
    private final MobEffectInstance mobEffectInstance;


    public TrimPower(int cooldown,TrimTier tier,MobEffectInstance mobEffectInstance) {
        this.cooldown = cooldown;
        this.tier = tier;
        this.mobEffectInstance = mobEffectInstance;
    }


    public void applyPassiveEffects(Player player) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        if (playerDuck.getTrimPower() > tier.passive) {
            player.addEffect(new MobEffectInstance(mobEffectInstance));
        }
    }

    public void activateAbility(Player player) {

    }

    protected static boolean addMobEffect(Player player, Holder<MobEffect> mobEffect, int amplifier) {
        return player.addEffect(new MobEffectInstance(mobEffect,MobEffectInstance.INFINITE_DURATION,amplifier,false,false,true));
    }

    protected static boolean addTempMobEffect(LivingEntity living, Holder<MobEffect> mobEffect, int amplifier, int time) {
        return living.addEffect(new MobEffectInstance(mobEffect,time,amplifier,false,false,true));
    }

    protected void playSound(ServerPlayer player) {
       // player.playNotifySound(ElixirSMPS2.ABILITY_USED, SoundSource.PLAYERS,1,1);
    }
}

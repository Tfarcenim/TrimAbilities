package tfar.trimabilities;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TrimPower {
    protected final int[] cooldowns;
    private final TrimTier tier;


    public TrimPower(int[] cooldowns,TrimTier tier) {
        this.cooldowns = cooldowns;
        this.tier = tier;
    }


    public void applyPassiveEffects(Player player) {

    }

    public void onEPChange(ServerPlayer player,int oldEP,int newEP) {

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

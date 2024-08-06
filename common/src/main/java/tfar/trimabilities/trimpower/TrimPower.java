package tfar.trimabilities.trimpower;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import tfar.trimabilities.PlayerDuck;
import tfar.trimabilities.TrimTier;

import java.util.EnumMap;
import java.util.function.Consumer;

public class TrimPower {
    protected final int cooldown;
    public final TrimTier tier;
    private final MobEffectInstance mobEffectInstance;
    private final Consumer<Player> consumer;



    public TrimPower(int cooldown, TrimTier tier, MobEffectInstance mobEffectInstance, Consumer<Player> consumer) {
        this.cooldown = cooldown;
        this.tier = tier;
        this.mobEffectInstance = mobEffectInstance;
        this.consumer = consumer;
    }


    public void applyPassiveEffects(Player player) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        if (playerDuck.getTrimPower() > tier.passive) {
            player.addEffect(new MobEffectInstance(mobEffectInstance));
        }
    }

    public void activateAbility(Player player, EquipmentSlot slot) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        if (playerDuck.getTrimPower() > tier.active) {
            EnumMap<EquipmentSlot,Integer> cooldowns = playerDuck.getCooldowns();
            Integer cooldown = cooldowns.get(slot);
            if (cooldown == null || cooldown <=0 ) {
                consumer.accept(player);
                cooldowns.put(slot,this.cooldown);
            }
        }
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

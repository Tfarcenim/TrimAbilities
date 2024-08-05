package tfar.trimabilities;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrimPowers {

    public static final Map<Holder<TrimPattern>, TrimPower> TRIM_MAP = new HashMap<>();
    public static final List<TrimPower> TRIM_POWER_LIST = new ArrayList<>();

    public static TrimPower SILENCE;
    public static TrimPower EYE;


    public static void registerPowers(MinecraftServer server) {
        TRIM_MAP.clear();
        TRIM_POWER_LIST.clear();
        HolderLookup.Provider registryAccess = server.registryAccess();
        SILENCE = register(get(registryAccess,TrimPatterns.SILENCE), new TrimPower(90 * 20,TrimTier.A,createTempEffect(MobEffects.DAMAGE_RESISTANCE,200,0),player -> {
            TargetingConditions conditions = TargetingConditions.forCombat();
            Level level = player.level();
            List<LivingEntity> nearby = level.getNearbyEntities(LivingEntity.class,conditions,player,player.getBoundingBox().inflate(40));
            for (LivingEntity living : nearby) {
                Vec3 vec3 = player.position();
                Vec3 vec31 = living.getEyePosition().subtract(vec3);
                Vec3 vec32 = vec31.normalize();
                int i = Mth.floor(vec31.length()) + 7;

                for (int j = 1; j < i; j++) {
                    Vec3 vec33 = vec3.add(vec32.scale(j));
                    ((ServerLevel) level).sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0, 0.0, 0.0, 0.0);
                }

                level.playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS,3.0F, 1.0F);
                if (living.hurt(level.damageSources().sonicBoom(player), 10.0F)) {
                    double d1 = 0.5 * (1.0 - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    double d0 = 2.5 * (1.0 - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    living.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
                }
            }
        }));
        EYE = register(get(registryAccess,TrimPatterns.EYE),new TrimPower(20 * 20,TrimTier.A,createTempEffect(MobEffects.REGENERATION,200,0),player -> {
            Level level = player.level();
            ThrownPotion thrownpotion = new ThrownPotion(level, player);
            ItemStack stack = PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_HEALING);
            thrownpotion.setItem(stack);
            thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(thrownpotion);
        }));
    }

    public static Holder<TrimPattern> get(HolderLookup.Provider access, ResourceKey<TrimPattern> key) {
        return access.lookup(Registries.TRIM_PATTERN).get().get(key).get();
    }

    public static MobEffectInstance createPermanentEffect(Holder<MobEffect> effect, int amplifier) {
        return new MobEffectInstance(effect,MobEffectInstance.INFINITE_DURATION,amplifier,false,false,true);
    }

    public static MobEffectInstance createTempEffect(Holder<MobEffect> effect,int ticks, int amplifier) {
        return new MobEffectInstance(effect,ticks,amplifier,false,false,true);
    }

    public static <P extends TrimPower> P register(Holder<TrimPattern> armorTrim, P power) {
        TRIM_MAP.put(armorTrim,power);
        TRIM_POWER_LIST.add(power);
        return power;
    }

    public static TrimPower getRandom(RandomSource random) {
        int i = random.nextInt(TRIM_POWER_LIST.size());
        return TRIM_POWER_LIST.get(i);
    }

}
//A tier:
//Silence trim
//Gives resistance 1 permanently when 1 piece worn
//allows the player to activate a sonic boom ability (shoots out a warden sonic boom) which does 5 hearts of damage every 90 seconds
//Eye trim
//Gives regeneration 1 permanently when 1 piece worn
//allows the player to splash an instant health 2 potion wherever they look every 20 seconds
//Ward trim
//Gives health boost 1 when 1 piece worn, health boost 2 when 2 piece worn
//allows player to activate ability which pushes back the player a 5 blocks every 60 seconds
//
//B tier:
//Dune
//Gives haste 2 permanently when 1 piece worn
//allows the player to activate a haste 6 ability (gives haste 6 for 10 seconds) every 60 seconds
//Sentry
//Gives strength 1 when 1 piece worn, strength 2 when 2 piece worn
//allows the player to activate an ability which shoots arrows out on all directions from the player, which cannot be picked up and deals 2 hearts to anyone hit by said arrows every 60 seconds
//Shaper
//Gives speed 1 when 1 piece worn, speed 2 when 2 piece worn
//allows the player to activate an ability which gives them speed 5 for 10 seconds every 60 seconds
//Tide
//Gives dolphins grace 1 when 1 piece worn
//allows the player to dash forward in or out of the water (riptide 3 trident pretty much) every 60 seconds
//Vex
//Gives invisibility 1 when 1 piece worn
//allows the player to teleport to the block they are looking at a maximum of 10 blocks away every 60 seconds
//Snout
//Fire resistance 1 when 1 piece worn
//Allows you to shoot a fireball with fireball strength 3 the way you are facing every 60 seconds
//
//B tier area of effect:
//Rib
//Allows the player to give every player in a radius of 5 blocks wither 3 for 10 seconds with a 60 second cooldown
//Bolt
//Allows the player to give every player in a radius of 5 blocks poison 3 for 10 seconds with a 60 second cooldown
//
//C tier:
//Flow
//Gives luck 2 when 1 piece worn
//Gives you a copy of the loot table of the chest you open within 10 seconds of activating the ability, only if the chest has never been loaded before (ie: open an ancient city chest for the first time, you get double the loot) every 5 minutes
//Host
//Bad omen 2 when 1 piece worn
//No ability
//Spire
//Levitation 2 when 1 piece worn
//Allows you to use a flight duration 3 rocket for free every 5 seconds
//Wild
//Hero of the village 2 when 1 piece worn
//Allows you to get hero of the village 5 for 30 seconds when used once every 3 minutes
//Coast
//Water breathing 1 when 1 piece worn
//Allows you to gain dolphins grace 2 for 5 seconds when used every 60 seconds
//Raiser
//Spawns a 1 second explosion delay tnt on your player when used every 60 seconds
//Wayfinder
//Slow falling 1 when 1 piece worn
//Teleports you to the nearest block below you when used every 30 seconds
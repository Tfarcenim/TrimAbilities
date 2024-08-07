package tfar.trimabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tfar.trimabilities.platform.MLConfig;
import tfar.trimabilities.platform.Services;
import tfar.trimabilities.trimpower.TrimPower;
import tfar.trimabilities.trimpower.SetBonusTrimPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrimPowers {

    public static final Map<Holder<TrimPattern>, TrimPower> TRIM_MAP = new HashMap<>();
    public static final List<TrimPower> TRIM_POWER_LIST = new ArrayList<>();

    public static TrimPower SILENCE;
    public static TrimPower EYE;
    public static TrimPower WARD;
    public static TrimPower DUNE;
    public static TrimPower SENTRY;
    public static TrimPower SHAPER;
    public static TrimPower TIDE;
    public static TrimPower VEX;
    public static TrimPower SNOUT;
    public static TrimPower RIB;
    public static TrimPower BOLT;
    public static TrimPower FLOW;
    public static TrimPower HOST;
    public static TrimPower SPIRE;
    public static TrimPower WILD;
    public static TrimPower COAST;
    public static TrimPower RAISER;
    public static TrimPower WAYFINDER;

    public static void registerPowers(MinecraftServer server) {
        TRIM_MAP.clear();
        TRIM_POWER_LIST.clear();
        HolderLookup.Provider registryAccess = server.registryAccess();
        MLConfig config = Services.PLATFORM.getConfig();


        SILENCE = register(get(registryAccess,TrimPatterns.SILENCE), new TrimPower(config.silenceCooldown(),TrimTier.A,createTempEffect(MobEffects.DAMAGE_RESISTANCE,200,0),player -> {
            TargetingConditions conditions = TargetingConditions.forCombat();
            Level level = player.level();
            List<Player> nearby = level.getNearbyPlayers(conditions,player,player.getBoundingBox().inflate(config.silenceRange()));
            if (!nearby.isEmpty()) {
                Player living = nearby.getFirst();
                Vec3 vec3 = player.position();
                Vec3 vec31 = living.getEyePosition().subtract(vec3);
                Vec3 vec32 = vec31.normalize();
                int i = Mth.floor(vec31.length()) + 7;

                for (int j = 1; j < i; j++) {
                    Vec3 vec33 = vec3.add(vec32.scale(j));
                    ((ServerLevel) level).sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0, 0.0, 0.0, 0.0);
                }

                level.playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS,3.0F, 1.0F);
                if (living.hurt(level.damageSources().sonicBoom(player), (float) config.silenceDamage())) {
                    double d1 = 0.5 * (1.0 - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    double d0 = 2.5 * (1.0 - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    living.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
                }
            }
        }));
        EYE = register(get(registryAccess,TrimPatterns.EYE),new TrimPower(config.eyeCooldown(),TrimTier.A,createTempEffect(MobEffects.REGENERATION,200,0),player -> {
            Level level = player.level();
            ThrownPotion thrownpotion = new ThrownPotion(level, player);
            ItemStack stack = PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_HEALING);
            thrownpotion.setItem(stack);
            thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(thrownpotion);
        }));
        WARD = register(get(registryAccess,TrimPatterns.WARD),new SetBonusTrimPower(config.wardCooldown(),TrimTier.A, player -> {
            TargetingConditions conditions = TargetingConditions.forCombat();
            Level level = player.level();
            List<LivingEntity> nearby = level.getNearbyEntities(LivingEntity.class,conditions,player,player.getBoundingBox().inflate(16));
            for (LivingEntity living : nearby) {
                Vec3 dir = player.position().subtract(living.position()).normalize().scale(config.wardRange());
                push(living,dir);
            }
        },MobEffects.HEALTH_BOOST));
        DUNE = register(get(registryAccess,TrimPatterns.DUNE),new TrimPower(config.duneCooldown(),TrimTier.B,createTempEffect(MobEffects.DIG_SPEED,200,1), player -> {
            player.addEffect(createTempEffect(MobEffects.DIG_SPEED,config.duneActiveLength(), config.duneActiveStrength() - 1));
        }));
        SENTRY = register(get(registryAccess,TrimPatterns.SENTRY),new SetBonusTrimPower(config.sentryCooldown(),TrimTier.B,player -> {
            int arrows = 8;

            for (int i = 0; i < arrows;i++) {
                double angle = i * 360d / arrows;
                double radians = angle * Math.PI / 180;
                double sin = Math.sin(radians);
                double cos = Math.cos(radians);
                ItemStack arrowStack = new ItemStack(Items.ARROW);
                arrowStack.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
                Arrow arrow = new Arrow(player.level(),player,arrowStack,null);
                arrow.setBaseDamage(config.sentryDamage());
                arrow.shoot(cos,0.1,sin,1,0);
                player.level().playSound(null,player.blockPosition(),SoundEvents.SKELETON_SHOOT,SoundSource.PLAYERS, 1.0F, 1.0F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                player.level().addFreshEntity(arrow);
            }

        },MobEffects.DAMAGE_BOOST));

        SHAPER = register(get(registryAccess,TrimPatterns.SHAPER),new SetBonusTrimPower(config.shaperCooldown(),TrimTier.B,player -> {
            player.addEffect(createTempEffect(MobEffects.MOVEMENT_SPEED,config.shaperActiveLength(),config.shaperActiveStrength() - 1));
        },MobEffects.MOVEMENT_SPEED));
        TIDE = register(get(registryAccess,TrimPatterns.TIDE),new TrimPower(config.tideCooldown(),TrimTier.B,createTempEffect(MobEffects.DOLPHINS_GRACE,200,0),player -> {
            float f = 3;
            float f7 = player.getYRot();
            float f1 = player.getXRot();
            float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
            float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
            float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
            float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
            f2 *= f / f5;
            f3 *= f / f5;
            f4 *= f / f5;
            player.push(f2, f3, f4);
            player.startAutoSpinAttack(20, 8.0F, player.getMainHandItem());
            if (player.onGround()) {
                float f6 = 1.2F;
                player.move(MoverType.SELF, new Vec3(0.0, f6, 0.0));
            }
            player.hurtMarked = true;
        }
        ));

        VEX = register(get(registryAccess,TrimPatterns.VEX),new TrimPower(config.vexCooldown(),TrimTier.B,createTempEffect(MobEffects.INVISIBILITY,200,0),player -> {
            HitResult pick = player.pick(config.vexRange(), 0, false);
            if (pick instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos().above();
                player.teleportTo(pos.getX(),pos.getY(),pos.getZ());
            }
        }
        ));

        SNOUT = register(get(registryAccess,TrimPatterns.SNOUT),new TrimPower(config.snoutCooldown(),TrimTier.B,createTempEffect(MobEffects.FIRE_RESISTANCE,200,0),player -> {
            Vec3 vec3 = player.getLookAngle();
            LargeFireball largeFireball = new LargeFireball(player.level(),player,vec3,3);
            largeFireball.setPos(player.getX(),player.getY()+player.getEyeHeight(),player.getZ());
            player.level().addFreshEntity(largeFireball);
        }
        ));

        RIB = register(get(registryAccess,TrimPatterns.RIB),new TrimPower(config.ribCooldown(),TrimTier.B,null, player -> {
            TargetingConditions conditions = TargetingConditions.forCombat();
            Level level = player.level();
            List<Player> nearby = level.getNearbyPlayers(conditions,player,player.getBoundingBox().inflate(config.ribActiveRange()));
            for (Player player1 : nearby) {
                player1.addEffect(createTempEffect(MobEffects.WITHER, config.ribActiveLength(), config.ribActiveStrength() - 1));
            }
        }));

        BOLT = register(get(registryAccess,TrimPatterns.BOLT),new TrimPower(config.boltCooldown(),TrimTier.B,null, player -> {
            TargetingConditions conditions = TargetingConditions.forCombat();
            Level level = player.level();
            List<Player> nearby = level.getNearbyPlayers(conditions,player,player.getBoundingBox().inflate(config.boltActiveRange()));
            for (Player player1 : nearby) {
                player1.addEffect(createTempEffect(MobEffects.POISON, config.boltActiveLength(), config.boltActiveStrength() - 1));
            }
        }));


        FLOW = register(get(registryAccess,TrimPatterns.FLOW),new TrimPower(config.flowCooldown(),TrimTier.C,createTempEffect(MobEffects.LUCK,200,1), player -> {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            playerDuck.setFlowTimer(config.flowActiveLength());
        }));

        HOST = register(get(registryAccess,TrimPatterns.FLOW),new TrimPower(config.hostCooldown(),TrimTier.C,createTempEffect(MobEffects.BAD_OMEN,200,1), player -> {
        }));

        SPIRE = register(get(registryAccess,TrimPatterns.SPIRE),new TrimPower(config.spireCooldown(),TrimTier.C,createTempEffect(MobEffects.LEVITATION,200,1), player -> {
            Level level = player.level();
            ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET);
            Fireworks fireworks = new Fireworks(4,List.of());
            itemstack.set(DataComponents.FIREWORKS,fireworks);
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(level, itemstack, player);
            level.addFreshEntity(fireworkrocketentity);
        }));

        WILD = register(get(registryAccess,TrimPatterns.WILD),new TrimPower(config.wildCooldown(),TrimTier.C,createTempEffect(MobEffects.HERO_OF_THE_VILLAGE,200,1), player -> {
            player.addEffect(createTempEffect(MobEffects.HERO_OF_THE_VILLAGE, config.wildActiveLength(), config.wildActiveStrength() - 1));
        }));

        COAST = register(get(registryAccess,TrimPatterns.COAST),new TrimPower(config.coastCooldown(),TrimTier.C,createTempEffect(MobEffects.WATER_BREATHING,200,0), player -> {
            player.addEffect(createTempEffect(MobEffects.DOLPHINS_GRACE, config.coastActiveLength(), config.coastActiveStrength() - 1));
        }));

        RAISER = register(get(registryAccess,TrimPatterns.RAISER),new TrimPower(config.raiserCooldown(),TrimTier.C,null, player -> {
            PrimedTnt primedTNT = new PrimedTnt(player.level(),player.getX(),player.getY(),player.getZ(),player);
            primedTNT.setFuse(config.raiserActiveDelay());
            player.level().addFreshEntity(primedTNT);
        }));

        WAYFINDER = register(get(registryAccess,TrimPatterns.WAYFINDER),new TrimPower(config.wayfinderCooldown(),TrimTier.C,createTempEffect(MobEffects.SLOW_FALLING,200,0), player -> {
            BlockPos pos = player.blockPosition();
            BlockPos offset = pos;
            boolean foundGround = false;

            while (!foundGround) {

                if (!player.level().getBlockState(offset).getCollisionShape(player.level(),offset).isEmpty()) {
                    foundGround = true;
                } else {
                    offset = offset.below();
                    if (offset.getY() <= player.level().getMinBuildHeight()) {
                        break;
                    }
                }
            }
            if (foundGround) {
                player.teleportTo(pos.getX(), offset.getY(), pos.getZ());
            }
        }));

    }

    protected static void push(LivingEntity living, Vec3 dir) {
        living.setDeltaMovement(living.getDeltaMovement().subtract(dir));
        living.hurtMarked = true;
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
package tfar.trimabilities;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import tfar.trimabilities.platform.MLConfig;

@Config(name = TrimAbilities.MOD_ID)
public class TrimAbilitiesClothConfig implements ConfigData, MLConfig {
    //server

    public int silence_cooldown = 90 * 20;
    public int eye_cooldown = 20 * 20;
    public int dune_cooldown = 60 * 20;
    public int ward_cooldown = 60 * 20;
    public int sentry_cooldown = 60 * 20;
    public int shaper_cooldown = 60 * 20;
    public int tide_cooldown = 60 * 20;
    public int vex_cooldown = 60 * 20;
    public int snout_cooldown = 60 * 20;
    public int rib_cooldown = 60 * 20;
    public int bolt_cooldown = 60 * 20;
    public int flow_cooldown = 5 * 60 * 20;
    public int host_cooldown = 0 * 20;
    public int spire_cooldown = 5 * 20;
    public int wild_cooldown = 3 * 60 * 20;
    public int coast_cooldown = 60 * 20;
    public int raiser_cooldown = 60 * 20;
    public int wayfinder_cooldown = 30 * 20;

    public double silence_damage = 10;
    public double silence_range = 10;
    public double ward_range = 5;

    public int dune_active_length = 10 * 20;
    public int dune_active_strength = 6;

    public double sentry_damage = 4;

    public int shaper_active_length = 200;
    public int shaper_active_strength = 5;

    public double vex_range = 10;

    public double rib_active_range = 5;
    public int rib_active_length = 200;
    public int rib_active_strength = 3;

    public double bolt_active_range = 5;
    public int bolt_active_length = 200;
    public int bolt_active_strength = 3;

    public int flow_active_length = 10 * 20;

    public int wild_active_length = 30 * 20;
    public int wild_active_strength = 5;

    public int coast_active_length = 5 * 20;
    public int coast_active_strength = 2;

    public int raiser_active_delay = 20;

    public int sentry_active_count = 8;

    @Override
    public int silenceCooldown() {
        return silence_cooldown;
    }

    @Override
    public int eyeCooldown() {
        return eye_cooldown;
    }

    @Override
    public int duneCooldown() {
        return dune_cooldown;
    }

    @Override
    public int wardCooldown() {
        return ward_cooldown;
    }

    @Override
    public int sentryCooldown() {
        return sentry_cooldown;
    }

    @Override
    public int shaperCooldown() {
        return shaper_cooldown;
    }

    @Override
    public int tideCooldown() {
        return tide_cooldown;
    }

    @Override
    public int vexCooldown() {
        return vex_cooldown;
    }

    @Override
    public int snoutCooldown() {
        return snout_cooldown;
    }

    @Override
    public int ribCooldown() {
        return rib_cooldown;
    }

    @Override
    public int boltCooldown() {
        return bolt_cooldown;
    }

    @Override
    public int flowCooldown() {
        return flow_cooldown;
    }

    @Override
    public int hostCooldown() {
        return host_cooldown;
    }

    @Override
    public int spireCooldown() {
        return spire_cooldown;
    }

    @Override
    public int wildCooldown() {
        return wild_cooldown;
    }

    @Override
    public int coastCooldown() {
        return coast_cooldown;
    }

    @Override
    public int raiserCooldown() {
        return raiser_cooldown;
    }

    @Override
    public int wayfinderCooldown() {
        return wayfinder_cooldown;
    }

    @Override
    public double silenceDamage() {
        return silence_damage;
    }

    @Override
    public double silenceRange() {
        return silence_range;
    }

    @Override
    public double wardRange() {
        return ward_range;
    }

    @Override
    public int duneActiveLength() {
        return dune_active_length;
    }

    @Override
    public int duneActiveStrength() {
        return dune_active_strength;
    }

    @Override
    public double sentryDamage() {
        return sentry_damage;
    }

    @Override
    public int shaperActiveLength() {
        return shaper_active_length;
    }

    @Override
    public int shaperActiveStrength() {
        return shaper_active_strength;
    }

    @Override
    public double vexRange() {
        return vex_range;
    }

    @Override
    public double ribActiveRange() {
        return rib_active_range;
    }

    @Override
    public int ribActiveLength() {
        return rib_active_length;
    }

    @Override
    public int ribActiveStrength() {
        return rib_active_strength;
    }

    @Override
    public double boltActiveRange() {
        return bolt_active_range;
    }

    @Override
    public int boltActiveLength() {
        return bolt_active_length;
    }

    @Override
    public int boltActiveStrength() {
        return bolt_active_strength;
    }

    @Override
    public int flowActiveLength() {
        return flow_active_length;
    }

    @Override
    public int wildActiveLength() {
        return wild_active_length;
    }

    @Override
    public int wildActiveStrength() {
        return wild_active_strength;
    }

    @Override
    public int coastActiveLength() {
        return coast_active_length;
    }

    @Override
    public int coastActiveStrength() {
        return coast_active_strength;
    }

    @Override
    public int raiserActiveDelay() {
        return raiser_active_delay;
    }

    @Override
    public int sentryActiveCount() {
        return sentry_active_count;
    }
}

package tfar.trimabilities.platform;

public interface MLConfig {

    int silenceCooldown();
    int eyeCooldown();
    int duneCooldown();
    int wardCooldown();
    int sentryCooldown();
    int shaperCooldown();
    int tideCooldown();
    int vexCooldown();
    int snoutCooldown();
    int ribCooldown();
    int boltCooldown();
    int flowCooldown();
    int hostCooldown();
    int spireCooldown();
    int wildCooldown();
    int coastCooldown();
    int raiserCooldown();
    int wayfinderCooldown();

    double silenceDamage();
    double silenceRange();

    double wardRange();

    int duneActiveLength();
    int duneActiveStrength();

    double sentryDamage();

    int shaperActiveLength();
    int shaperActiveStrength();

    double vexRange();

    double ribActiveRange();
    int ribActiveLength();
    int ribActiveStrength();

    double boltActiveRange();
    int boltActiveLength();
    int boltActiveStrength();

    int flowActiveLength();

    int wildActiveLength();
    int wildActiveStrength();

    int coastActiveLength();
    int coastActiveStrength();

    int raiserActiveDelay();

    int sentryActiveCount();
}

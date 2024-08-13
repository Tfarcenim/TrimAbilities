package tfar.trimabilities.mixin;

import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.trimabilities.TNTDuck;

@Mixin(PrimedTnt.class)
public class PrimedTNTMixin implements TNTDuck {

    boolean dontDamageOwner;
    float power;

    @Override
    public boolean dontDamageOwner() {
        return dontDamageOwner;
    }

    @Override
    public void setDontDamageOwner(boolean dontDamageOwner) {
        this.dontDamageOwner = dontDamageOwner;
    }

    @Override
    public float getPower() {
        return power;
    }

    @Override
    public void setPower(float power) {
        this.power = power;
    }

    @ModifyArg(method = "explode",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"))
    private float usePower(float old) {
        return power;
    }
}

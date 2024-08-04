package tfar.trimabilities.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.trimabilities.PlayerDuck;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerDuck {

    @Shadow public abstract void tick();

    @Unique
    int trimPower;

    @Unique
    int[] cooldowns = new int[6];

    protected PlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }


    @Override
    public int getTrimPower() {
        return trimPower;
    }

    @Override
    public void setTrimPower(int trimPower) {
        this.trimPower = trimPower;
    }

    @Override
    public void setTrimPowerNoUpdate(int trimPower) {
        this.trimPower = trimPower;
    }


    @Override
    public int[] getCooldowns() {
        return cooldowns;
    }

    @Inject(method = "tick",at = @At("HEAD"))
    private void onPlayerTick(CallbackInfo ci) {
        if (!level().isClientSide) {
            tickServer();
        }
    }

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addAdd(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("trim_power", trimPower);
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readAdd(CompoundTag tag, CallbackInfo ci) {
        trimPower = tag.getInt("trim_power");
    }

}

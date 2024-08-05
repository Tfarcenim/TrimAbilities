package tfar.trimabilities.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.trimabilities.PlayerDuck;

import java.util.EnumMap;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerDuck {

    @Unique
    int trimPower;

    @Unique
    EquipmentSlot ability1;
    @Unique
    EquipmentSlot ability2;
    @Unique
    EnumMap<EquipmentSlot,Integer> cooldowns = new EnumMap<>(EquipmentSlot.class);

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
    public EnumMap<EquipmentSlot, Integer> getCooldowns() {
        return cooldowns;
    }

    @Override
    public EquipmentSlot getAbility1() {
        return ability1;
    }

    @Override
    public EquipmentSlot getAbility2() {
        return ability2;
    }

    @Override
    public void setAbility1(EquipmentSlot ability1) {
        this.ability1 = ability1;
    }

    @Override
    public void setAbility2(EquipmentSlot ability2) {
        this.ability2 = ability2;
    }

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addAdd(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("trim_power", trimPower);
        if (ability1 != null) {
            tag.putInt("ability1", ability1.ordinal());
        }
        if (ability2 != null) {
            tag.putInt("ability2", ability2.ordinal());
        }
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readAdd(CompoundTag tag, CallbackInfo ci) {
        trimPower = tag.getInt("trim_power");
        if (tag.contains("ability1")) {
            ability1 = EquipmentSlot.values()[tag.getInt("ability1")];
        }
        if (tag.contains("ability2")) {
            ability2 = EquipmentSlot.values()[tag.getInt("ability2")];
        }
    }

}

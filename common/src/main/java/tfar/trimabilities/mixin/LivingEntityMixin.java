package tfar.trimabilities.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.trimabilities.TrimAbilities;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onEquipItem",at = @At("HEAD"))
    private void onEquipItemEvent(EquipmentSlot pSlot, ItemStack pOldItem, ItemStack pNewItem, CallbackInfo ci) {
        TrimAbilities.onItemEquipped((LivingEntity)(Object) this,pSlot,pOldItem,pNewItem);
    }

}

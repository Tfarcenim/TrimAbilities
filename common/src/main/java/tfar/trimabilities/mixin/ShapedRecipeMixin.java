package tfar.trimabilities.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.trimabilities.TrimAbilities;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {


    @Shadow @Final
    ItemStack result;

    @Inject(method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",at = @At("RETURN"),cancellable = true)
    private void checkTrimPower(CraftingInput pInput, Level pLevel, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (!TrimAbilities.onAttemptCraft(pInput,result)) {
            cir.setReturnValue(false);
        }
    }

}

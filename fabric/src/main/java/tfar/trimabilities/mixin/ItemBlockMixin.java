package tfar.trimabilities.mixin;


import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.trimabilities.TrimAbilitiesFabric;

@Mixin(BlockItem.class)
public class ItemBlockMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void onBlockAttemptedPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir){
        InteractionResult interactionResult = TrimAbilitiesFabric.attemptPlace(context);
        if (interactionResult != InteractionResult.PASS) {
            cir.setReturnValue(interactionResult);
        }
    }
}
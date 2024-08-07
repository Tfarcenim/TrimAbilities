package tfar.trimabilities.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.trimabilities.ModCommands;

@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin {

    @Inject(method = "createArmorTrimTemplate(Lnet/minecraft/resources/ResourceKey;[Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/item/SmithingTemplateItem;",at = @At("RETURN"))
    private static void makeMap(ResourceKey<TrimPattern> pKey, FeatureFlag[] pRequiredFeatures, CallbackInfoReturnable<SmithingTemplateItem> cir) {
        ModCommands.MAP.put(pKey,cir.getReturnValue());
    }

}

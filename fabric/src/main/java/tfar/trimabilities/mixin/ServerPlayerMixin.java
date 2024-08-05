package tfar.trimabilities.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.trimabilities.TrimAbilities;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "tick",at = @At("HEAD"))
    private void tickEvent(CallbackInfo ci) {
        TrimAbilities.playerTick((ServerPlayer) (Object)this);
    }
}

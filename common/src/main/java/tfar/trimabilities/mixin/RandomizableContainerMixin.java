package tfar.trimabilities.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.trimabilities.PlayerDuck;

@Mixin(RandomizableContainer.class)
public interface RandomizableContainerMixin {

    @Shadow long getLootTableSeed();

    @Inject(method = "unpackLootTable",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V")
    ,locals = LocalCapture.CAPTURE_FAILHARD)
    private void fill2(Player pPlayer, CallbackInfo ci, Level level, BlockPos blockpos, ResourceKey resourcekey, LootTable loottable, LootParams.Builder lootparams$builder) {
        PlayerDuck playerDuck = PlayerDuck.of(pPlayer);
        if (playerDuck.getFlowTimer() > 0) {
            loottable.fill((RandomizableContainer) this, lootparams$builder.create(LootContextParamSets.CHEST), this.getLootTableSeed());
        }
    }

}

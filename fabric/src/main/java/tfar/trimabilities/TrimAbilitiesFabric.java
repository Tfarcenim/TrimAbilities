package tfar.trimabilities;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import tfar.trimabilities.init.ModItems;

public class TrimAbilitiesFabric implements ModInitializer {

    public static TrimAbilitiesClothConfig CONFIG;

    @Override
    public void onInitialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register(TrimAbilities::onDeath);
        ServerPlayerEvents.COPY_FROM.register(TrimAbilities::onClone);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> TrimAbilities.onLogin(handler.player));
        ServerLifecycleEvents.SERVER_STARTED.register(TrimAbilities::onServerStarted);
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (world.isClientSide) return InteractionResultHolder.pass(stack);

            if (player.isSpectator()) return InteractionResultHolder.pass(stack);
            if (ItemStack.isSameItemSameComponents(stack, ModItems.TRIM_POWER)) {
                PlayerDuck.of(player).addTrimPower(1);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                ModCommands.spawnWitchParticles((ServerPlayer) player);
                ModCommands.playUpSound((ServerPlayer) player);
            } else if (ItemStack.isSameItemSameComponents(stack,ModItems.REVIVE_HEAD)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResultHolder.pass(stack);
        });
        AutoConfig.register(TrimAbilitiesClothConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TrimAbilitiesClothConfig.class).getConfig();
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        TrimAbilities.init();
    }
}

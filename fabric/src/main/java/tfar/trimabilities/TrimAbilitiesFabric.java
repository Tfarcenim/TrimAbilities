package tfar.trimabilities;

import com.mojang.authlib.GameProfile;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import tfar.trimabilities.init.ModItems;

import java.util.List;

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
                ((ServerPlayer) player).sendSystemMessage(Component.literal("New trim power is "+PlayerDuck.of(player).getTrimPower()),false);

            } else if (ItemStack.isSameItemSameComponents(stack, ModItems.REVIVE_HEAD)) {
                boolean b = listBannedPlayers((ServerPlayer) player);
                if (b && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResultHolder.success(stack);
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

    public static InteractionResult attemptPlace(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        if (ItemStack.isSameItemSameComponents(ModItems.REVIVE_HEAD,stack)) {
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    public static boolean listBannedPlayers(ServerPlayer player) {
        SimpleGui onlineGui = new SimpleGui(MenuType.GENERIC_9x6, player, false);
        onlineGui.setTitle(Component.literal("Deathbanned Players"));
        List<GameProfile> bannedPlayers = ModCommands.getDeathBanned(player.server);

        if (bannedPlayers.isEmpty()) return false;

        UserBanList userbanlist = player.server.getPlayerList().getBans();


        final int pages = (int) Math.ceil(bannedPlayers.size() / 45f);
        if (pages > 1) {
            int[] page = new int[]{0};
            updateOnlinePage(onlineGui, page[0], bannedPlayers,player.server);

            onlineGui.setSlot(45, new GuiElementBuilder(Items.ARROW)
                    .setName(Component.literal("left"))
                    .setCallback((index1, type1, action1, gui) -> {
                        if (page[0] >= 1) {
                            page[0]--;
                            updateOnlinePage(onlineGui, page[0], bannedPlayers,player.server);
                        }
                    })
            );

            onlineGui.setSlot(53, new GuiElementBuilder(Items.ARROW)
                    .setName(Component.literal("right"))
                    .setCallback((index1, type1, action1, gui) -> {
                        if (page[0] < pages - 1) {
                            page[0]++;
                            updateOnlinePage(onlineGui, page[0], bannedPlayers,player.server);
                        }
                    })
            );

        } else {
            for (int i = 0; i < bannedPlayers.size(); i++) {
                GameProfile gameProfile = bannedPlayers.get(i);

                String string = gameProfile.getName();

                onlineGui.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                        .setSkullOwner(gameProfile, player.server)
                        .setName(Component.literal(string)).setCallback((i1, clickType, clickType1) -> {
                            userbanlist.remove(gameProfile);
                            player.sendSystemMessage(Component.literal("Revived " + gameProfile.getName()));
                            onlineGui.close();
                        }));
            }
        }
        onlineGui.open();
        return true;
    }

    private static void updateOnlinePage(SimpleGui simpleGui, int page, List<GameProfile> players, MinecraftServer server) {

        UserBanList userbanlist = server.getPlayerList().getBans();


        for (int i = 0; i < 45; i++) {
            int offsetSlot = page * 45 + i;
            if (offsetSlot < players.size()) {
                GameProfile offsetPlayer = players.get(offsetSlot);
                String string = offsetPlayer.getName();

                simpleGui.setSlot(i, new GuiElementBuilder(Items.PLAYER_HEAD)
                        .setSkullOwner(offsetPlayer, server)
                        .setName(Component.literal(string)).setCallback((i1, clickType, clickType1) -> {
                            userbanlist.remove(offsetPlayer);
                        })
                );
            } else {
                simpleGui.setSlot(i, ItemStack.EMPTY);
            }
        }
    }

}

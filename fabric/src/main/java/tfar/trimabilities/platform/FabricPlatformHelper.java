package tfar.trimabilities.platform;

import dev.architectury.platform.Mod;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import tfar.trimabilities.PlayerDuck;
import tfar.trimabilities.TrimAbilities;
import tfar.trimabilities.TrimAbilitiesFabric;
import tfar.trimabilities.init.ModItems;
import tfar.trimabilities.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public MLConfig getConfig() {
        return TrimAbilitiesFabric.CONFIG;
    }

    @Override
    public boolean checkBasicPermission(CommandSourceStack commandSourceStack, String node, int defaultValue) {
        return commandSourceStack.hasPermission(defaultValue);
    }

    @Override
    public void openAbilityScreen(ServerPlayer player) {
        SimpleGui simpleGui = new SimpleGui(MenuType.GENERIC_9x6,player,false);
        simpleGui.setTitle(Component.literal("Select Abilities"));

        PlayerDuck playerDuck = PlayerDuck.of(player);

        EquipmentSlot ability1 = playerDuck.getAbility1();
        EquipmentSlot ability2 = playerDuck.getAbility2();
        for (int i = 0; i < 4;i++) {
            EquipmentSlot slot = TrimAbilities.armor[i];
            ItemStack helmet = player.getItemBySlot(slot);
            ItemStack starter = ability1 == slot || ability2 == slot ? ModItems.YES : ModItems.NO;

            simpleGui.setSlot(12 + 9 * i, new GuiElementBuilder(helmet));

            simpleGui.setSlot(14 + 9 * i, new GuiElementBuilder(starter)
                    .setCallback((index, clickType, actionType) -> {
                        if (clickType == ClickType.MOUSE_LEFT && slot != ability1 && slot != ability2) {
                            playerDuck.setAbility1(slot);
                            GuiElementInterface guiElementInterface = simpleGui.getSlot(index);
                            for (int j = 0; j < 4;j++) {
                                EquipmentSlot other = TrimAbilities.armor[j];
                                if (other == slot) continue;
                                if (other == ability2) continue;

                                GuiElementInterface guiElementInterface2 = simpleGui.getSlot(14 + 9 * j);
                                ((GuiElement)guiElementInterface2).setItemStack(ModItems.NO);
                            }

                            ((GuiElement)guiElementInterface).setItemStack(ModItems.YES);

                        } else if (clickType == ClickType.MOUSE_RIGHT && slot != ability1 && slot != ability2) {
                            playerDuck.setAbility2(slot);
                            GuiElementInterface guiElementInterface = simpleGui.getSlot(index);

                            for (int j = 0; j < 4;j++) {
                                EquipmentSlot other = TrimAbilities.armor[j];
                                if (other == slot) continue;
                                if (other == ability1) continue;

                                GuiElementInterface guiElementInterface2 = simpleGui.getSlot(14 + 9 * j);
                                ((GuiElement)guiElementInterface2).setItemStack(ModItems.NO);
                            }

                            ((GuiElement)guiElementInterface).setItemStack(ModItems.YES);
                        }
                    })
            );
        }
        simpleGui.open();
    }
}

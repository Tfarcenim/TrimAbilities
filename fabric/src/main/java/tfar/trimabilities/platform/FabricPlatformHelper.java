package tfar.trimabilities.platform;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import tfar.trimabilities.PlayerDuck;
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

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        simpleGui.setSlot(14, new GuiElementBuilder(helmet));

        simpleGui.setSlot(16, new GuiElementBuilder(ModItems.YES)
                .setName(Component.literal("Yes"))
                .setCallback((index, clickType, actionType) -> {

                })
        );
    }


}

package tfar.trimabilities.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class ModItems {

    public static final ItemStack TRIM_POWER = createTrimPower();
    public static final ItemStack REVIVE_HEAD = createReviveHead();

    public static ItemStack createTrimPower() {
        ItemStack trimPoint = new ItemStack(Items.BLACK_DYE);
        trimPoint.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,true);
        trimPoint.set(DataComponents.RARITY, Rarity.RARE);
        trimPoint.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
        trimPoint.set(DataComponents.CUSTOM_NAME, Component.literal("Trim Power").setStyle(Style.EMPTY.withItalic(false)));
        return trimPoint;
    }

    public static ItemStack createReviveHead() {
        ItemStack trimPoint = new ItemStack(Items.GOLD_BLOCK);
        trimPoint.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,true);
        trimPoint.set(DataComponents.RARITY, Rarity.RARE);
        trimPoint.set(DataComponents.CUSTOM_NAME, Component.literal("Revive Head").setStyle(Style.EMPTY.withItalic(false)));
        return trimPoint;
    }

}

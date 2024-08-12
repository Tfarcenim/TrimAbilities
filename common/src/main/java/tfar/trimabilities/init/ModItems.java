package tfar.trimabilities.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomModelData;

public class ModItems {

    public static final ItemStack TRIM_POWER = createTrimPower();
    public static final ItemStack REVIVE_HEAD = createReviveHead();
    public static final ItemStack YES = yes();
    public static final ItemStack NO = no();


    public static ItemStack createTrimPower() {
        ItemStack trimPoint = new ItemStack(Items.BLACK_DYE);
        trimPoint.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,true);
        trimPoint.set(DataComponents.RARITY, Rarity.RARE);
        trimPoint.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
        trimPoint.set(DataComponents.CUSTOM_NAME, Component.literal("Trim Power").setStyle(Style.EMPTY.withItalic(false)));
        trimPoint.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(1));
        return trimPoint;
    }

    public static ItemStack createReviveHead() {
        ItemStack reviveHead = new ItemStack(Items.GOLD_BLOCK);
        reviveHead.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,true);
        reviveHead.set(DataComponents.RARITY, Rarity.RARE);
        reviveHead.set(DataComponents.CUSTOM_NAME, Component.literal("Revive Head").setStyle(Style.EMPTY.withItalic(false)));
        reviveHead.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(2));
        return reviveHead;
    }

    public static ItemStack yes() {
        ItemStack yes = new ItemStack(Items.GREEN_STAINED_GLASS_PANE);
        yes.set(DataComponents.CUSTOM_NAME,Component.literal("Enabled").setStyle(Style.EMPTY.withItalic(false)));
        return yes;
    }

    public static ItemStack no() {
        ItemStack yes = new ItemStack(Items.RED_STAINED_GLASS_PANE);
        yes.set(DataComponents.CUSTOM_NAME,Component.literal("Disabled").setStyle(Style.EMPTY.withItalic(false)));
        return yes;
    }

}

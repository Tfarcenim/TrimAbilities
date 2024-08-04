package tfar.trimabilities.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tfar.trimabilities.TrimAbilities;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries);
        this.registries = registries;
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ItemStack trimPoint = new ItemStack(Items.BLACK_DYE);
        trimPoint.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE,true);
        trimPoint.set(DataComponents.CUSTOM_NAME, Component.literal("Trim Power").setStyle(Style.EMPTY.withItalic(false)));


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,trimPoint)
                .define('e',Items.END_CRYSTAL)
                .define('n',Items.NETHERITE_INGOT)
                .define('w',Items.WITHER_SKELETON_SKULL)
                .define('t',Items.TOTEM_OF_UNDYING)
                .define('g',Items.GOLDEN_APPLE)
                .pattern("ene")
                .pattern("wtw")
                .pattern("ege")
                .unlockedBy(getHasName(Items.END_CRYSTAL),has(Items.END_CRYSTAL))
                .save(recipeOutput,TrimAbilities.id("trim_power_point"));
        ;
    }
}

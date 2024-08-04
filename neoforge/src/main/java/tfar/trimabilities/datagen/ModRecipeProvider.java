package tfar.trimabilities.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import tfar.trimabilities.TrimAbilities;
import tfar.trimabilities.init.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries);
        this.registries = registries;
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TRIM_POWER)
                .define('e',Items.END_CRYSTAL)
                .define('n',Items.NETHERITE_INGOT)
                .define('w',Items.WITHER_SKELETON_SKULL)
                .define('t',Items.TOTEM_OF_UNDYING)
                .define('g',Items.GOLDEN_APPLE)
                .pattern("ene")
                .pattern("wtw")
                .pattern("ege")
                .unlockedBy(getHasName(Items.END_CRYSTAL),has(Items.END_CRYSTAL))
                .save(recipeOutput,TrimAbilities.id("trim_power"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REVIVE_HEAD)
                .define('e',Items.DIAMOND_BLOCK)
                .define('n',Items.GOLDEN_APPLE)
                .define('w',Items.ENDER_PEARL)
                .define('t',Items.BOOK)
                .define('g',Items.BLACK_DYE)
                .pattern("ene")
                .pattern("wtw")
                .pattern("ege")
                .unlockedBy(getHasName(Items.DIAMOND_BLOCK),has(Items.DIAMOND_BLOCK))
                .save(recipeOutput,TrimAbilities.id("revive_head"));
    }
}

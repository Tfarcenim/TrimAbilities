package tfar.trimabilities.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ModDatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput output = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        dataGenerator.addProvider(true,new ModRecipeProvider(output,lookup));
    }
}

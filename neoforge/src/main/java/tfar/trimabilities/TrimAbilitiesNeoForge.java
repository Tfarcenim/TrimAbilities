package tfar.trimabilities;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import tfar.trimabilities.datagen.ModDatagen;

@Mod(TrimAbilities.MOD_ID)
public class TrimAbilitiesNeoForge {

    public TrimAbilitiesNeoForge(IEventBus eventBus) {
        eventBus.addListener(ModDatagen::gather);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        TrimAbilities.init();

    }
}
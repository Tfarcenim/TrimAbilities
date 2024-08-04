package tfar.trimabilities;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class TrimAbilitiesFabric implements ModInitializer {

    public static TrimAbilitiesClothConfig CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(TrimAbilitiesClothConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TrimAbilitiesClothConfig.class).getConfig();
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        TrimAbilities.init();
    }
}

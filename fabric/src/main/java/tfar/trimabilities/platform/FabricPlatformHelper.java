package tfar.trimabilities.platform;

import net.minecraft.commands.CommandSourceStack;
import tfar.trimabilities.TrimAbilitiesFabric;
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


}

package tfar.trimabilities.platform;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import tfar.trimabilities.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public MLConfig getConfig() {
        return null;
    }

    @Override
    public boolean checkBasicPermission(CommandSourceStack commandSourceStack, String node, int defaultValue) {
        return commandSourceStack.hasPermission(defaultValue);
    }

    @Override
    public void openAbilityScreen(ServerPlayer player) {

    }
}
package tfar.trimabilities;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.trimabilities.init.ModItems;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TrimAbilities {

    public static final String MOD_ID = "trimabilities";
    public static final String MOD_NAME = "TrimAbilities";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);


    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
    }

    public static void onDeath(LivingEntity living, DamageSource damageSource) {
        if (living instanceof ServerPlayer serverPlayer) {
            PlayerDuck playerDuck = PlayerDuck.of(serverPlayer);

            ItemEntity itemEntity = new ItemEntity(living.level(), living.getX(), living.getY(), living.getZ(), ModItems.TRIM_POWER);
            itemEntity.setUnlimitedLifetime();
            living.level().addFreshEntity(itemEntity);
            playerDuck.addTrimPower(-1);
            if (playerDuck.getTrimPower() < -3) {
                deathBan(serverPlayer);
            }
        }
    }

    public static void onLogin(ServerPlayer player) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        if (playerDuck.getTrimPower() < -3) {
            playerDuck.setTrimPower(0);
        }
    }

    public static final MutableComponent DEATH_BAN_MESSAGE = Component.empty()
            .append(Component.literal("You are death banned!").withStyle(ChatFormatting.RED))
            .append("\n")
            .append("\n")
            .append(Component.literal("Try to get someone to revive you!"));

    public static void deathBan(ServerPlayer player) {
        UserBanList userbanlist = player.getServer().getPlayerList().getBans();
        GameProfile gameprofile = player.getGameProfile();
        CommandSourceStack commandSourceStack = player.getServer().createCommandSourceStack();

        UserBanListEntry userbanlistentry = new UserBanListEntry(
                gameprofile, null, commandSourceStack.getTextName(), null, ModCommands.DEATHBAN.getString());
        userbanlist.add(userbanlistentry);
        commandSourceStack.sendSuccess(
                () -> Component.translatable("commands.ban.success", Component.literal(gameprofile.getName()), userbanlistentry.getReason()), true
        );
        player.connection.disconnect(DEATH_BAN_MESSAGE);
    }

    public static void onClone(ServerPlayer originalPlayer, ServerPlayer newPlayer, boolean alive) {
        PlayerDuck.of(originalPlayer).copyTo(newPlayer);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
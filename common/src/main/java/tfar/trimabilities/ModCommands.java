package tfar.trimabilities;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.commands.BanPlayerCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.BanListEntry;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import tfar.trimabilities.init.ModItems;
import tfar.trimabilities.platform.Services;

import java.util.Collection;
import java.util.function.Predicate;

public class ModCommands {
    public static final String POWER_POWER = "power.power";
    public static final String ELIXIR_COOLDOWN = "elixir.cooldown";

    static final String POWER = "trim_power";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("power")
                .requires(permissionNode("power", 2))
                /* .then(Commands.literal("start")
                         .requires(permissionNode(ELIXIR_START, 2))
                         .executes(ModCommands::start))
                 .then(Commands.literal("stop")
                         .requires(permissionNode(ELIXIR_STOP, 2))
                         .executes(ModCommands::stop))*/
                .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument(POWER, IntegerArgumentType.integer()).executes(ModCommands::powerAdd))))
                .then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.player()).executes(ModCommands::powerGet)))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument(POWER, IntegerArgumentType.integer(-3, 3))
                                        .executes(ModCommands::powerSet))))

                .then(Commands.literal("deathban")
                        .then(Commands.literal("manual")
                                .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                                        .executes(ModCommands::deathbanManual)
                                )
                        )
                        .then(Commands.literal("list").executes(ModCommands::deathbanList)
                        )
                )
                .then(Commands.literal("reset")
                        .executes(ModCommands::powerReset)
                )
                .then(Commands.literal("give")
                        .then(Commands.literal("power")
                                .executes(ModCommands::givePowerItem)
                        )
                        .then(Commands.literal("revive")
                                .executes(ModCommands::giveReviveItem)
                        )
                )
        );

        dispatcher.register(Commands.literal("withdraw")
                .then(Commands.argument(POWER, IntegerArgumentType.integer(1))
                        .executes(ModCommands::powerWithdraw)
                )
        );
    }


    public static int powerAdd(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        int points = IntegerArgumentType.getInteger(ctx, POWER);
        PlayerDuck.of(player).addTrimPower(points);
        return 1;
    }

    public static int powerWithdraw(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();//EntityArgument.getPlayer(ctx, "player");
        int points = IntegerArgumentType.getInteger(ctx, POWER);
        PlayerDuck playerDuck = PlayerDuck.of(player);
        int curPoints = playerDuck.getTrimPower();
        if (curPoints < -3) return 0;
        if (curPoints - points < -4) return 0;
        playerDuck.setTrimPower(-points);
        ItemStack stack = ModItems.TRIM_POWER.copyWithCount(points);
        if (!player.addItem(stack)) {
            player.level().addFreshEntity(new ItemEntity(player.serverLevel(), player.getX(), player.getY(), player.getZ(), stack));
        }
        playSound(player);
        spawnWitchParticles(player);
        return 1;
    }

    protected static void playSound(ServerPlayer player) {
        player.playNotifySound(SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1, 1);
    }

    public static void playUpSound(ServerPlayer player) {
        player.playNotifySound(SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1, 1);
    }

    public static void spawnWitchParticles(ServerPlayer player) {
        RandomSource random = player.getRandom();
        for (int i = 0; i < random.nextInt(35) + 10; i++) {
            player.level()
                    .addParticle(
                            ParticleTypes.WITCH,
                            player.getX() + random.nextGaussian() * 0.13F,
                            player.getBoundingBox().maxY + 0.5 + random.nextGaussian() * 0.13F,
                            player.getZ() + random.nextGaussian() * 0.13F,
                            0.0,
                            0.0,
                            0.0
                    );
        }
    }

    public static int powerGet(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        int points = PlayerDuck.of(player).getTrimPower();
        ctx.getSource().sendSuccess(() -> Component.empty().append(player.getName()).append(Component.literal(" has " + points + " trim power")), false);
        return 1;
    }

    public static int powerSet(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        int points = IntegerArgumentType.getInteger(ctx, POWER);
        PlayerDuck.of(player).setTrimPower(points);
        return 1;
    }

    public static final MutableComponent DEATHBAN = Component.literal("deathban");

    public static int deathbanManual(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<GameProfile> targets = GameProfileArgument.getGameProfiles(ctx, "targets");
        BanPlayerCommands.banPlayers(ctx.getSource(), targets, DEATHBAN);
        return targets.size();
    }

    public static int deathbanList(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack pSource = ctx.getSource();
        Collection<UserBanListEntry> pBannedPlayerList = ctx.getSource().getServer().getPlayerList().getBans().getEntries().stream()
                .filter(userBanListEntry -> DEATHBAN.getString().equals(userBanListEntry.getReason())).toList();
        if (pBannedPlayerList.isEmpty()) {
            pSource.sendSuccess(() -> Component.translatable("commands.banlist.none"), false);
        } else {
            pSource.sendSuccess(() -> Component.translatable("commands.banlist.list", pBannedPlayerList.size()), false);

            for (BanListEntry<?> banlistentry : pBannedPlayerList) {
                pSource.sendSuccess(
                        () -> Component.translatable("commands.banlist.entry", banlistentry.getDisplayName(), banlistentry.getSource(), banlistentry.getReason()),
                        false
                );
            }
        }

        return pBannedPlayerList.size();
    }

    public static int powerReset(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().getServer().getPlayerList().getPlayers().forEach(player -> PlayerDuck.of(player).setTrimPower(0));
        ctx.getSource().sendSuccess(() -> Component.literal("Reset all players' trim power"), false);
        return 1;
    }

    public static int givePowerItem(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        return giveOneItem(ModItems.TRIM_POWER, player);
    }

    public static int giveReviveItem(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        return giveOneItem(ModItems.REVIVE_HEAD, player);

    }

    public static int giveOneItem(ItemStack stack, ServerPlayer player) {
        ItemEntity itementity = player.drop(stack, false);
        if (itementity != null) {
            itementity.setNoPickUpDelay();
            itementity.setTarget(player.getUUID());
        }
        return 1;
    }

    public static Predicate<CommandSourceStack> permissionNode(String node, int defaultValue) {
        return commandSourceStack -> Services.PLATFORM.checkBasicPermission(commandSourceStack, node, defaultValue);
    }

}

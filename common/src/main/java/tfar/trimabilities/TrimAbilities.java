package tfar.trimabilities;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.trimabilities.init.ModItems;
import tfar.trimabilities.trimpower.TrimPower;

import java.util.EnumMap;
import java.util.Objects;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TrimAbilities {

    public static final String MOD_ID = "trimabilities";
    public static final String MOD_NAME = "TrimAbilities";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static boolean ENABLED = true;


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
        if (!TrimAbilities.ENABLED) return;
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

    public static final EquipmentSlot[] armor = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static void onClone(ServerPlayer originalPlayer, ServerPlayer newPlayer, boolean alive) {
        PlayerDuck oldPlayerDuck = PlayerDuck.of(originalPlayer);
        PlayerDuck newPlayerDuck = PlayerDuck.of(newPlayer);
        newPlayerDuck.setTrimPowerNoUpdate(oldPlayerDuck.getTrimPower());

        newPlayerDuck.setAbility1(oldPlayerDuck.getAbility1());
        newPlayerDuck.setAbility2(oldPlayerDuck.getAbility2());

    }

    public static ArmorTrim getTrim(ItemStack stack) {
        return stack.get(DataComponents.TRIM);
    }


    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }


    public static void playerTick(ServerPlayer player) {
        if (!ENABLED) return;
        boolean clientDirty = true;
        PlayerDuck playerDuck = PlayerDuck.of(player);
        Object2IntMap<Holder<TrimPattern>> patternCount = new Object2IntOpenHashMap<>();
        for (EquipmentSlot equipmentSlot : armor) {
            ItemStack stack = player.getItemBySlot(equipmentSlot);
            Integer value = playerDuck.getCooldowns().get(equipmentSlot);
            if (value != null && value > 0) {
                value--;
                playerDuck.getCooldowns().put(equipmentSlot, value);
                clientDirty = true;
            }
            if (player.tickCount % 20 == 0) {
                ArmorTrim armorTrim = getTrim(stack);
                if (armorTrim != null) {
                    Holder<TrimPattern> pattern = armorTrim.pattern();

                    patternCount.put(pattern, patternCount.getInt(pattern) + 1);

                    TrimPower trimPower = TrimPowers.TRIM_MAP.get(pattern);
                    if (trimPower != null) {
                        trimPower.applyPassiveEffects(player);
                    }
                }
            }
        }

        if (player.tickCount % 20 == 0) {
            for (Object2IntMap.Entry<Holder<TrimPattern>> entry : patternCount.object2IntEntrySet()) {
                Holder<TrimPattern> wardPattern = entry.getKey();
                int count = entry.getIntValue();
                TrimPower trimPower = TrimPowers.TRIM_MAP.get(wardPattern);
                if (trimPower != null) {
                    trimPower.applySetBonus(player, count);
                }
            }
        }

        if (playerDuck.getFlowTimer() > 0) {
            playerDuck.setFlowTimer(playerDuck.getFlowTimer() - 1);
        }

        if (clientDirty) {
            MutableComponent component = Component.empty();
            EquipmentSlot slot1 = playerDuck.getAbility1();
            ArmorTrim armorTrim1 = null;
            if (slot1 != null) {
                armorTrim1 = player.getItemBySlot(slot1).get(DataComponents.TRIM);
            }


            if (slot1 != null) {
                if (armorTrim1 != null) {
                    Holder<TrimPattern> pattern = armorTrim1.pattern();
                    component.append(CommonComponents.space().append(pattern.value().copyWithStyle(armorTrim1.material())));
                } else {
                    component.append("Empty");
                }
                component.append(": ");

                Integer ticks = playerDuck.getCooldowns().get(slot1);
                if (ticks != null && ticks > 0) {
                    String sec = String.format("%.0f", ticks / 20d);
                    component.append(sec);
                } else {
                    component.append(Component.literal("\u2714").withStyle(ChatFormatting.GREEN));
                }

                component.append(Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY));
            }

            EquipmentSlot slot2 = playerDuck.getAbility2();
            if (slot2 != null) {
                ItemStack stack = player.getItemBySlot(slot2);
                ArmorTrim armorTrim2 = getTrim(stack);

                if (armorTrim2 != null && !Objects.equals(armorTrim2,armorTrim1)) {
                    Holder<TrimPattern> pattern = armorTrim2.pattern();
                        component.append(CommonComponents.space().append(pattern.value().copyWithStyle(armorTrim2.material())));
                } else {
                    component.append("Empty");
                }

                component.append(": ");

                Integer ticks = playerDuck.getCooldowns().get(slot2);
                if (ticks != null && ticks > 0) {
                    String sec = String.format("%.0f", ticks / 20d);
                    component.append(sec);
                } else {
                    component.append(Component.literal("\u2714").withStyle(ChatFormatting.GREEN));
                }
            }

            player.displayClientMessage(component, true);
        }

    }

    //[ABILITY 1 TRIM NAME]: [SECONDS] | [ABILITY 2 TRIM NAME]: [SECONDS]

    public static void onItemEquipped(LivingEntity living,EquipmentSlot slot,ItemStack oldItem,ItemStack newItem) {
        if (living instanceof ServerPlayer serverPlayer && slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            if (!newItem.isEmpty()) {
                ArmorTrim armorTrim = newItem.get(DataComponents.TRIM);
                if (armorTrim != null) {
                    TrimPower trimPower = TrimPowers.TRIM_MAP.get(armorTrim.pattern());
                    if (trimPower != null) {
                        EnumMap<EquipmentSlot, Integer> cooldowns = PlayerDuck.of(serverPlayer).getCooldowns();
                        Integer cooldown = cooldowns.get(slot);
                        if (cooldown == null || cooldown <= 0) {
                            cooldowns.put(slot, trimPower.cooldown);
                        }
                    }
                }
            }
        }
    }

    public static MutableComponent prefix() {
        return Component.literal("<Server> ");
    }

    public static void onServerStarted(MinecraftServer server) {
        TrimPowers.registerPowers(server);
    }
}
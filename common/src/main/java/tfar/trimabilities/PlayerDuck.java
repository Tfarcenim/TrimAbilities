package tfar.trimabilities;

import net.minecraft.world.entity.player.Player;

public interface PlayerDuck {

    int getTrimPower();
    void setTrimPower(int trimPower);
    void setTrimPowerNoUpdate(int points);
    default void addTrimPower(int points) {
        setTrimPower(getTrimPower() + points);
    }

    int[] getCooldowns();

    default void copyTo(Player newPlayer) {
        PlayerDuck newPlayerDuck = of(newPlayer);
        newPlayerDuck.setTrimPowerNoUpdate(getTrimPower());
    }

    default void tickServer() {
        boolean clientDirty = false;
        int[] cooldowns = getCooldowns();
        for (int i = 0; i < cooldowns.length;i++) {
            if (cooldowns[i] > 0) {
                cooldowns[i]--;
                clientDirty = true;
            }
        }
        if (clientDirty) {

        }
    }

    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }

}

package tfar.trimabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;
import java.util.Map;

public interface PlayerDuck {

    int getTrimPower();
    void setTrimPower(int trimPower);
    void setTrimPowerNoUpdate(int points);
    default void addTrimPower(int points) {
        setTrimPower(getTrimPower() + points);
    }

    EnumMap<EquipmentSlot,Integer> getCooldowns();
    EquipmentSlot getAbility1();
    EquipmentSlot getAbility2();

    void setAbility1(EquipmentSlot ability1);
    void setAbility2(EquipmentSlot ability2);

    int getFlowTimer();
    void setFlowTimer(int flowTimer);

    default void copyTo(Player newPlayer) {

    }


    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }

}

package tfar.trimabilities;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.armortrim.ArmorTrim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrimPowers {

    public static final Map<ArmorTrim, TrimPower> TRIM_MAP = new HashMap<>();
    public static final List<TrimPower> TRIM_POWER_LIST = new ArrayList<>();




    public static <P extends TrimPower> P register(ArmorTrim armorTrim, P power) {
        TRIM_MAP.put(armorTrim,power);
        TRIM_POWER_LIST.add(power);
        return power;
    }

    public static TrimPower getRandom(RandomSource random) {
        int i = random.nextInt(TRIM_POWER_LIST.size());
        return TRIM_POWER_LIST.get(i);
    }

}

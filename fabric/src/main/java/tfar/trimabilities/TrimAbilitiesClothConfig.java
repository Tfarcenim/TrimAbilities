package tfar.trimabilities;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import tfar.trimabilities.platform.MLConfig;

@Config(name = TrimAbilities.MOD_ID)
public class TrimAbilitiesClothConfig implements ConfigData, MLConfig {
    //server

    public int potion_stack_size = 16;
    public int splash_potion_stack_size = 4;
    public int lingering_potion_stack_size = 4;

    public int potion_use_cooldown = 30;
    public int potion_throw_cooldown = 30;

    public int gauntlet_cooldown = 600;
    public int coating_uses = 25;
    public boolean spike_food = true;
    public boolean show_spiked_food = true;
    public boolean milk = true;

}

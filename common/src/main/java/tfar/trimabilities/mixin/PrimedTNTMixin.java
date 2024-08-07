package tfar.trimabilities.mixin;

import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import tfar.trimabilities.TNTDuck;

@Mixin(PrimedTnt.class)
public class PrimedTNTMixin implements TNTDuck {

    boolean dontDamageOwner;

    @Override
    public boolean dontDamageOwner() {
        return dontDamageOwner;
    }

    @Override
    public void setDontDamageOwner(boolean dontDamageOwner) {
        this.dontDamageOwner = dontDamageOwner;
    }
}

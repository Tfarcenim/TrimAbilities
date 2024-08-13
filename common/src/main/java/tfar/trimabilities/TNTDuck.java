package tfar.trimabilities;

import net.minecraft.world.entity.item.PrimedTnt;

public interface TNTDuck {

    boolean dontDamageOwner();
    void setDontDamageOwner(boolean dontDamageOwner);

    float getPower();
    void setPower(float power);
    static TNTDuck of(PrimedTnt tnt) {
        return (TNTDuck) tnt;
    }

}

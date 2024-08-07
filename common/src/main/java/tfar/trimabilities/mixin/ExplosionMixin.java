package tfar.trimabilities.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.trimabilities.TNTDuck;

import java.util.List;
import java.util.Set;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow @Final private Entity source;

    @Inject(method = "explode",at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void preventSelfDamage(CallbackInfo ci, Set set,int i, float f2, int k1, int l1, int i2, int i1, int j2, int j1, List<Entity> list, Vec3 vec3) {
        if (this.source instanceof PrimedTnt primedTnt && ((TNTDuck)primedTnt).dontDamageOwner()) {
            list.remove(primedTnt.getOwner());
        }
    }
}

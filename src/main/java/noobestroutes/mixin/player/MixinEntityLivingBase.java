package noobestroutes.mixin.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import noobestroutes.events.impl.MoveEntityWithHeadingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static noobestroutes.utils.UtilsKt.postAndCatch;


@Mixin(value = EntityLivingBase.class, priority = 9000)
public abstract class MixinEntityLivingBase {

    @Shadow protected abstract boolean isPlayer();

    @Inject(
            method = {"moveEntityWithHeading"},
            at = @At("HEAD"),
            require = 0,
            cancellable = true
    )
    private void noobestroutes$onMoveEntityWithHeadingPre(float strafe, float forward, CallbackInfo ci) {
        if ((Object) this instanceof EntityPlayerSP) {
            if (postAndCatch(new MoveEntityWithHeadingEvent.Pre())) ci.cancel();
        }
    }
    @Inject(
            method = {"moveEntityWithHeading"},
            at = @At("TAIL"),
            require = 0,
            cancellable = true
    )
    private void noobestroutes$onMoveEntityWithHeadingPost(float strafe, float forward, CallbackInfo ci) {
        if ((Object) this instanceof EntityPlayerSP) {
            if (postAndCatch(new MoveEntityWithHeadingEvent.Post())) ci.cancel();
        }
    }


}

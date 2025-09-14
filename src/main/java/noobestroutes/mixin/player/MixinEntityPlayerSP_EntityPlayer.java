package noobestroutes.mixin.player;

import com.mojang.authlib.GameProfile;
import noobestroutes.events.impl.MotionUpdateEvent;
import noobestroutes.utils.PlayerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static noobestroutes.utils.UtilsKt.postAndCatch;

/**
 * Motion Update Event Taken From CGA
 */
@Mixin(value = {EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP_EntityPlayer extends EntityPlayer {
    @Shadow private int positionUpdateTicks;

    @Shadow public abstract boolean isSneaking();

    @Unique
    private double noobestroutes$oldPosX;
    @Unique
    private double noobestroutes$oldPosY;
    @Unique
    private double noobestroutes$oldPosZ;
    @Unique
    private float noobestroutes$oldYaw;
    @Unique
    private float noobestroutes$oldPitch;
    @Unique
    private boolean noobestroutes$oldOnGround;

    public MixinEntityPlayerSP_EntityPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }


    @Inject(method = "onUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true)
    public void noobestroutes$onUpdatePre(CallbackInfo ci) {
        this.noobestroutes$oldPosX = this.posX;
        this.noobestroutes$oldPosY = this.posY;
        this.noobestroutes$oldPosZ = this.posZ;

        this.noobestroutes$oldYaw = this.rotationYaw;
        this.noobestroutes$oldPitch = this.rotationPitch;

        this.noobestroutes$oldOnGround = this.onGround;

        MotionUpdateEvent.Pre motionUpdateEvent = new MotionUpdateEvent.Pre(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround);

        if (postAndCatch(motionUpdateEvent)) ci.cancel();

        this.posX = motionUpdateEvent.x;
        this.posY = motionUpdateEvent.y;
        this.posZ = motionUpdateEvent.z;

        this.rotationYaw = motionUpdateEvent.yaw;
        this.rotationPitch = motionUpdateEvent.pitch;

        this.onGround = motionUpdateEvent.onGround;
    }

    @Inject(
            method = "onUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void noobestroutes$onUpdatePost(CallbackInfo ci) {
        this.posX = this.noobestroutes$oldPosX;
        this.posY = this.noobestroutes$oldPosY;
        this.posZ = this.noobestroutes$oldPosZ;

        this.rotationYaw = this.noobestroutes$oldYaw;
        this.rotationPitch = this.noobestroutes$oldPitch;

        this.onGround = this.noobestroutes$oldOnGround;

        MotionUpdateEvent.Post motionUpdateEvent = new MotionUpdateEvent.Post(posX, posY, posZ, motionX, motionY, motionZ, rotationYaw, rotationPitch, onGround);

        if (postAndCatch(motionUpdateEvent)) ci.cancel();

        this.posX = motionUpdateEvent.x;
        this.posY = motionUpdateEvent.y;
        this.posZ = motionUpdateEvent.z;

        this.rotationYaw = motionUpdateEvent.yaw;
        this.rotationPitch = motionUpdateEvent.pitch;

        this.onGround = motionUpdateEvent.onGround;
    }

}

package modid.mixin.player;

import com.mojang.authlib.GameProfile;
import modid.events.impl.MotionUpdateEvent;
import modid.utils.PlayerUtils;
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

import static modid.utils.UtilsKt.postAndCatch;

/**
 * Motion Update Event Taken From CGA
 */
@Mixin(value = {EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP_EntityPlayer extends EntityPlayer {
    @Shadow private int positionUpdateTicks;

    @Shadow public abstract boolean isSneaking();

    @Unique
    private double modid$oldPosX;
    @Unique
    private double modid$oldPosY;
    @Unique
    private double modid$oldPosZ;
    @Unique
    private float modid$oldYaw;
    @Unique
    private float modid$oldPitch;
    @Unique
    private boolean modid$oldOnGround;

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
    public void modid$onUpdatePre(CallbackInfo ci) {
        this.modid$oldPosX = this.posX;
        this.modid$oldPosY = this.posY;
        this.modid$oldPosZ = this.posZ;

        this.modid$oldYaw = this.rotationYaw;
        this.modid$oldPitch = this.rotationPitch;

        this.modid$oldOnGround = this.onGround;

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
    public void modid$onUpdatePost(CallbackInfo ci) {
        this.posX = this.modid$oldPosX;
        this.posY = this.modid$oldPosY;
        this.posZ = this.modid$oldPosZ;

        this.rotationYaw = this.modid$oldYaw;
        this.rotationPitch = this.modid$oldPitch;

        this.onGround = this.modid$oldOnGround;

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

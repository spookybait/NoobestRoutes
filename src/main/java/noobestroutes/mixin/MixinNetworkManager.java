/*package noobestroutes.mixin;

import io.netty.channel.ChannelHandlerContext;
import noobestroutes.events.impl.PacketEvent;
import noobestroutes.utils.ClientUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static noobestroutes.utils.UtilsKt.postAndCatch;


}*/

package noobestroutes.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import noobestroutes.events.impl.PacketEvent;
import noobestroutes.events.impl.PacketReturnEvent;
import noobestroutes.utils.ServerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static noobestroutes.utils.UtilsKt.postAndCatch;

@Mixin(value = {NetworkManager.class}, priority = 1003)
public class MixinNetworkManager {

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    private void noobestroutes$onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (postAndCatch(new PacketEvent.Receive(packet)) && !ci.isCancelled()) ci.cancel();
    }


    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
    private void noobestroutes$onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (!ServerUtils.handleSendPacket(packet) && !ServerUtils.isServerPacket(packet))
            if (postAndCatch(new PacketEvent.Send(packet)) && !ci.isCancelled()) ci.cancel();
    }

    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("RETURN")})
    private void noobestroutes$onSendPacketReturn(Packet<?> packet, CallbackInfo ci) {
        postAndCatch(new PacketReturnEvent.Send(packet));
    }

    @Inject(method = "channelRead0*", at = {@At("RETURN")})
    private void noobestroutes$onReceivePacketReturn(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        postAndCatch(new PacketReturnEvent.Receive(packet));
    }

}


package modid.mixin;

import modid.events.impl.ClickEvent;
import modid.events.impl.InputEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static modid.utils.UtilsKt.postAndCatch;

@Mixin(value = {Minecraft.class}, priority = 800)
public class MixinMinecraft {
    @Inject(method = {"runTick"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V")})
    public void modid$keyPresses(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) postAndCatch(new InputEvent.Keyboard((Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + 256) : Keyboard.getEventKey()));
    }

    @Inject(method = {"runTick"}, at = {@At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventButton()I", remap = false)})
    public void modid$mouseKeyPresses(CallbackInfo ci) {
        if (Mouse.getEventButtonState()) postAndCatch(new InputEvent.Mouse(Mouse.getEventButton()));
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    private void modid$rightClickMouse(CallbackInfo ci) {
        if (postAndCatch(new ClickEvent.Right()) || postAndCatch(new ClickEvent.All(ClickEvent.ClickType.Right))) ci.cancel();

    }

    @Inject(method = "middleClickMouse", at = @At("HEAD"), cancellable = true)
    private void modid$middleClickMouse(CallbackInfo ci) {
        if (postAndCatch(new ClickEvent.Middle()) || postAndCatch(new ClickEvent.All(ClickEvent.ClickType.Middle))) ci.cancel();
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void modid$clickMouse(CallbackInfo ci) {
        if (postAndCatch(new ClickEvent.Left()) || postAndCatch(new ClickEvent.All(ClickEvent.ClickType.Left))) ci.cancel();
    }
}
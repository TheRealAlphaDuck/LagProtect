package com.lagprotect.mixin;

import com.lagprotect.CustomKeepAliveAccess;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundKeepAlivePacket.class)
public abstract class KeepAlivePacketMixin {

    @Inject(
            method = "handle(Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void customKeepAlive(ServerCommonPacketListener serverCommonPacketListener, CallbackInfo ci) {
        ((CustomKeepAliveAccess)serverCommonPacketListener).handleCustomKeepAlive((ServerboundKeepAlivePacket) (Object)this);
        if (((ServerboundKeepAlivePacket) (Object)this).getId() <= 11000L) {
            ci.cancel();
        }
    }
}

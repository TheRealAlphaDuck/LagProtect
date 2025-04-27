package com.lagprotect.mixin;

import com.lagprotect.CustomKeepAliveAccess;
import com.lagprotect.LagProtect;
import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("all")
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin extends ServerCommonPacketListenerImpl implements CustomKeepAliveAccess {
    @Unique
    long customChallenge = 1;
    @Unique
    boolean customPending = false;
    @Unique
    long protectUntil = 0;

    @Shadow public ServerPlayer player;

    @Shadow private int tickCount;

    public ServerGamePacketListenerMixin(MinecraftServer minecraftServer, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraftServer, connection, commonListenerCookie);
    }

    @Unique
    public void handleCustomKeepAlive(ServerboundKeepAlivePacket keepAlivePacket) {
        if (customPending && keepAlivePacket.getId() == this.customChallenge) {
            customPending = false;
        }
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void keepAlive(CallbackInfo ci) {
        int gameRule = server.getGameRules().getInt(LagProtect.LAG_PROTECT);
        if (tickCount % gameRule == 0) {
            if (customPending) {
                player.setInvulnerable(true);
                if (Util.getMillis() >= protectUntil) {
                    LagProtect.LOGGER.info("[Lag Protect] {} protected", player.getName().getString());
                }
                protectUntil = Util.getMillis()+1000L;
            }
            if (Util.getMillis() >= protectUntil) {
                if (player.isInvulnerable()) {
                    player.setInvulnerable(false);
                    LagProtect.LOGGER.info("[Lag Protect] {} no longer protected ", player.getName().getString());
                }
            }
            customChallenge++;
            if (customChallenge > 10000L) {
                customChallenge = 1;
            }
            this.send(new ClientboundKeepAlivePacket(customChallenge));
            customPending = true;
        }
    }

}

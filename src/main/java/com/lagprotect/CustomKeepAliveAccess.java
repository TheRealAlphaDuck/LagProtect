package com.lagprotect;

import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;

public interface CustomKeepAliveAccess {
    void handleCustomKeepAlive(ServerboundKeepAlivePacket keepAlivePacket);
}

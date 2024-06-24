package com.matthewperiut.entris.network;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerNetworkHelper {
    public static void send(ServerPlayerEntity player, CustomPayload payload) {
        player.networkHandler.send(new CustomPayloadS2CPacket(payload), null);
    }
}

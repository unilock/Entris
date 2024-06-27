package com.matthewperiut.entris.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerNetworkHelper {
    public static void send(ServerPlayerEntity player, FabricPacket payload) {
        ServerPlayNetworking.send(player, payload);
    }
}

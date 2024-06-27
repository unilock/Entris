package com.matthewperiut.entris.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;

import java.util.Objects;

public class ClientNetworkHelper {
    public static void send(FabricPacket payload) {
        Objects.requireNonNull(payload, "Payload cannot be null");
        Objects.requireNonNull(payload.getType().getId(), "FabricPacket#getType#getId cannot return null for payload class: " + payload.getClass());

        ClientPlayNetworking.send(payload);
    }
}

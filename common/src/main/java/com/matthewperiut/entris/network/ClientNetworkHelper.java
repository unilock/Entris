package com.matthewperiut.entris.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import java.util.Objects;


public class ClientNetworkHelper {
    public static void send(CustomPayload payload) {
        Objects.requireNonNull(payload, "Payload cannot be null");
        Objects.requireNonNull(payload.getId(), "CustomPayload#getId() cannot return null for payload class: " + payload.getClass());

        // You cant send without a client player, so this is fine
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(payload));

            return;
        }

        throw new IllegalStateException("Cannot send packets when not in game!");
    }
}

package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.network.client.HandleAllowEntrisPayload;
import com.matthewperiut.entris.network.client.HandleValidEntrisScorePayload;
import com.matthewperiut.entris.network.payload.AllowEntrisPayload;
import com.matthewperiut.entris.network.payload.ValidEntrisScorePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;


public class EntrisClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(AllowEntrisPayload.TYPE, (payload, player, responseSender) -> {
            MinecraftClient.getInstance().execute(() -> {
                HandleAllowEntrisPayload.handle(payload.allow());
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ValidEntrisScorePayload.TYPE, (payload, player, responseSender) -> {
            MinecraftClient.getInstance().execute(() -> {
                HandleValidEntrisScorePayload.handle(payload.score());
            });
        });
    }
}

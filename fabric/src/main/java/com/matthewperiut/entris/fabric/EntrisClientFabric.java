package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.client.ClientEntrisInterface;
import com.matthewperiut.entris.network.payload.AllowEntrisPayload;
import com.matthewperiut.entris.network.payload.RequestStartEntrisPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.screen.EnchantmentScreenHandler;


public class EntrisClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(AllowEntrisPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (MinecraftClient.getInstance().currentScreen instanceof EnchantmentScreen enchantmentScreenHandler) {
                    if (payload.allow())
                        ((ClientEntrisInterface)enchantmentScreenHandler).beginGame();
                    else
                        ((ClientEntrisInterface)enchantmentScreenHandler).errorHandling();
                }
            });
        });
    }
}

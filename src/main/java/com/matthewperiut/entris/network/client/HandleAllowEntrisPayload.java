package com.matthewperiut.entris.network.client;

import com.matthewperiut.entris.client.ClientEntrisInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;

public class HandleAllowEntrisPayload {
    public static void handle(boolean allow)
    {
        if (MinecraftClient.getInstance().currentScreen instanceof EnchantmentScreen enchantmentScreenHandler) {
            if (allow)
                ((ClientEntrisInterface)enchantmentScreenHandler).beginGame();
            else
                ((ClientEntrisInterface)enchantmentScreenHandler).errorHandling();
        }
    }
}

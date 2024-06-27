package com.matthewperiut.entris.network.client;

import com.matthewperiut.entris.client.ClientEntrisInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;

public class HandleValidEntrisScorePayload {
    public static void handle(int score) {
        if (MinecraftClient.getInstance().currentScreen instanceof EnchantmentScreen enchantmentScreenHandler) {
            if (score > 0)
                ((ClientEntrisInterface)enchantmentScreenHandler).validifyScore(score);
            else
                ((ClientEntrisInterface)enchantmentScreenHandler).errorHandling();
        }
    }
}

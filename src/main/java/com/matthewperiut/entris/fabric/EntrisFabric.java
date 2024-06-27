package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.network.payload.FinishEntrisPayload;
import com.matthewperiut.entris.network.payload.RequestEntrisEnchantsPayload;
import com.matthewperiut.entris.network.payload.RequestStartEntrisPayload;
import com.matthewperiut.entris.network.server.HandleFinishEntrisPayload;
import com.matthewperiut.entris.network.server.HandleRequestEntrisEnchantsPayload;
import com.matthewperiut.entris.network.server.HandleRequestStartEntrisPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.matthewperiut.entris.game.SoundHelper.*;

public class EntrisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Entris.init();

        Registry.register(Registries.SOUND_EVENT, MOVE_SOUND_ID, MOVE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, HARD_DROP_SOUND_ID, HARD_DROP_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, FINALIZE_SOUND_ID, FINALIZE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, ROTATE_SOUND_ID, ROTATE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, SWAP_SOUND_ID, SWAP_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, TICK_SOUND_ID, TICK_SOUND_EVENT);

        ServerPlayNetworking.registerGlobalReceiver(RequestStartEntrisPayload.TYPE, (payload, player, responseSender) -> {
            player.getServer().execute(() -> {
                HandleRequestStartEntrisPayload.handle(player, payload.levels());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(FinishEntrisPayload.TYPE, (payload, player, responseSender) -> {
            player.getServer().execute(() -> {
                HandleFinishEntrisPayload.handle(player, payload.score());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(RequestEntrisEnchantsPayload.TYPE, (payload, player, responseSender) -> {
            player.getServer().execute(() -> {
                HandleRequestEntrisEnchantsPayload.handle(player.getServer(), player, payload.enchants());
            });
        });
    }
}

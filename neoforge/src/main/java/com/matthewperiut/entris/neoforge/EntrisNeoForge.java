package com.matthewperiut.entris.neoforge;

import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.network.client.HandleAllowEntrisPayload;
import com.matthewperiut.entris.network.client.HandleValidEntrisScorePayload;
import com.matthewperiut.entris.network.payload.*;
import com.matthewperiut.entris.network.server.HandleFinishEntrisPayload;
import com.matthewperiut.entris.network.server.HandleRequestEntrisEnchantsPayload;
import com.matthewperiut.entris.network.server.HandleRequestStartEntrisPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@Mod(Entris.MOD_ID)
public class EntrisNeoForge {
    public EntrisNeoForge(IEventBus modEventBus, ModContainer modContainer)
    {
        Entris.init();
        modEventBus.register(EntrisNeoForge.class);
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        System.out.println("THISISBEINGREGED");
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                RequestStartEntrisPayload.ID,
                RequestStartEntrisPayload.CODEC,
                new DirectionalPayloadHandler<>(
                        ((payload, context) -> {
                        }),
                        ((payload, context) -> {
                            //server
                            HandleRequestStartEntrisPayload.handle((ServerPlayerEntity) context.player(), payload.levels());
                        })
                )
        );
        registrar.playBidirectional(
                FinishEntrisPayload.ID,
                FinishEntrisPayload.CODEC,
                new DirectionalPayloadHandler<>(
                        ((payload, context) -> {

                        }),
                        ((payload, context) -> {
                            //server
                            HandleFinishEntrisPayload.handle((ServerPlayerEntity) context.player(), payload.score());
                        })
                )
        );
        registrar.playBidirectional(
                RequestEntrisEnchantsPayload.ID,
                RequestEntrisEnchantsPayload.CODEC,
                new DirectionalPayloadHandler<>(
                        ((payload, context) -> {

                        }),
                        ((payload, context) -> {
                            // server
                            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                            HandleRequestEntrisEnchantsPayload.handle(server, (ServerPlayerEntity) context.player(), payload.enchants());
                        })
                )
        );
        registrar.playBidirectional(
                AllowEntrisPayload.ID,
                AllowEntrisPayload.CODEC,
                new DirectionalPayloadHandler<>(
                        ((payload, context) -> {
                            // client
                            HandleAllowEntrisPayload.handle(payload.allow());
                        }),
                        ((payload, context) -> {

                        })
                )
        );
        registrar.playBidirectional(
                ValidEntrisScorePayload.ID,
                ValidEntrisScorePayload.CODEC,
                new DirectionalPayloadHandler<>(
                        ((payload, context) -> {
                            //client
                            HandleValidEntrisScorePayload.handle(payload.score());
                        }),
                        ((payload, context) -> {

                        })
                )
        );
    }
}

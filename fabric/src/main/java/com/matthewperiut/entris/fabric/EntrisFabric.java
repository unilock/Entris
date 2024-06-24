package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.BookShelvesUtil;
import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.client.SlotEnabler;
import com.matthewperiut.entris.enchantment.EnchantmentHelp;
import com.matthewperiut.entris.network.payload.*;
import com.matthewperiut.entris.network.server.HandleFinishEntrisPayload;
import com.matthewperiut.entris.network.server.HandleRequestEntrisEnchantsPayload;
import com.matthewperiut.entris.network.server.HandleRequestStartEntrisPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static com.matthewperiut.entris.game.SoundHelper.*;
import static com.matthewperiut.entris.game.SoundHelper.TICK_SOUND_EVENT;
import static net.minecraft.util.math.MathHelper.ceil;

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

        PayloadTypeRegistry.playC2S().register(RequestStartEntrisPayload.ID, RequestStartEntrisPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AllowEntrisPayload.ID, AllowEntrisPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FinishEntrisPayload.ID, FinishEntrisPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ValidEntrisScorePayload.ID, ValidEntrisScorePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestEntrisEnchantsPayload.ID, RequestEntrisEnchantsPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestStartEntrisPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                HandleRequestStartEntrisPayload.handle(context.player(), payload.levels());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(FinishEntrisPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                HandleFinishEntrisPayload.handle(context.player(), payload.score());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(RequestEntrisEnchantsPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                HandleRequestEntrisEnchantsPayload.handle(context.server(), context.player(), payload.enchants());
            });
        });
    }
}

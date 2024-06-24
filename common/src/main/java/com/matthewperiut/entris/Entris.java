package com.matthewperiut.entris;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.matthewperiut.entris.game.SoundHelper.*;

public class Entris {
    public static boolean disableRegularEnchanting = true;

    public static final String MOD_ID = "entris";

    public static void init() {

        Registry.register(Registries.SOUND_EVENT, MOVE_SOUND_ID, MOVE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, HARD_DROP_SOUND_ID, HARD_DROP_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, FINALIZE_SOUND_ID, FINALIZE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, ROTATE_SOUND_ID, ROTATE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, SWAP_SOUND_ID, SWAP_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, TICK_SOUND_ID, TICK_SOUND_EVENT);
    }
}
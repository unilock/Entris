package com.matthewperiut.entris.game;


import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundHelper {

    public static final Identifier MOVE_SOUND_ID = Identifier.of("entris:move");
    public static SoundEvent MOVE_SOUND_EVENT = SoundEvent.of(MOVE_SOUND_ID);
    public static final Identifier HARD_DROP_SOUND_ID = Identifier.of("entris:hard_drop");
    public static SoundEvent HARD_DROP_SOUND_EVENT = SoundEvent.of(HARD_DROP_SOUND_ID);
    public static final Identifier FINALIZE_SOUND_ID = Identifier.of("entris:finalize");
    public static SoundEvent FINALIZE_SOUND_EVENT = SoundEvent.of(FINALIZE_SOUND_ID);
    public static final Identifier ROTATE_SOUND_ID = Identifier.of("entris:rotate");
    public static SoundEvent ROTATE_SOUND_EVENT = SoundEvent.of(ROTATE_SOUND_ID);
    public static final Identifier SWAP_SOUND_ID = Identifier.of("entris:swap");
    public static SoundEvent SWAP_SOUND_EVENT = SoundEvent.of(SWAP_SOUND_ID);
    public static final Identifier TICK_SOUND_ID = Identifier.of("entris:tick");
    public static SoundEvent TICK_SOUND_EVENT = SoundEvent.of(TICK_SOUND_ID);



    public static void moveSound() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(MOVE_SOUND_EVENT, 1.0F, 1.0F);
        }
    }

    public static void hardDropSound() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(HARD_DROP_SOUND_EVENT, 1.0F, 1.0F);
        }
    }

    public static void finalizeSound() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(FINALIZE_SOUND_EVENT, 1.0F, 1.0F);
        }
    }

    public static void rotateSound() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(ROTATE_SOUND_EVENT, 1.0F, 1.0F);
        }
    }

    public static void swapSound() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(SWAP_SOUND_EVENT, 1.0F, 1.0F);
        }
    }

    public static void tickSound() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player != null) {
            minecraft.player.playSound(TICK_SOUND_EVENT, 1.0F, 1.0F);
        }
    }

/*
    private static SoundInstance pigstepSoundInstance;

    public static void playPigstep() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        pigstepSoundInstance = SoundInstance.forMusic(SoundEvents.MUSIC_DISC_PIGSTEP);
        minecraft.getSoundManager().play(pigstepSoundInstance);
    }

    public static void stopPigstep() {
        if (pigstepSoundInstance != null) {
            MinecraftClient.getInstance().getSoundManager().stop(pigstepSoundInstance);
            pigstepSoundInstance = null; // Clear the reference once stopped
        }
    }*/
}
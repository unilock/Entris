package com.matthewperiut.entris;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.HashMap;
import java.util.Queue;

import static com.matthewperiut.entris.game.SoundHelper.*;

public class Entris {
    public static boolean disableRegularEnchanting = true;

    public static final int MARGIN_OF_ERROR_TIME = 10;

    public static class PlayerData {
        public long timeStamp;
        public int allottedTime;
        public int score = 0;

        public PlayerData(long timeStamp, int allottedTime) {
            this.timeStamp = timeStamp;
            this.allottedTime = allottedTime;
        }
    }
    public static HashMap<PlayerEntity, PlayerData> playerDataMap = new HashMap<>();

    public static final String MOD_ID = "entris";

    public static void init() {

    }
}
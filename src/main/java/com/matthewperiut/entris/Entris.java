package com.matthewperiut.entris;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

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

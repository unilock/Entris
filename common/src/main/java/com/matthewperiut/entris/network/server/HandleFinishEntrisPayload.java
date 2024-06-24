package com.matthewperiut.entris.network.server;

import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.network.ServerNetworkHelper;
import com.matthewperiut.entris.network.payload.ValidEntrisScorePayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.matthewperiut.entris.Entris.playerDataMap;

public class HandleFinishEntrisPayload {
    public static void handle(ServerPlayerEntity player, int score)
    {
        Entris.PlayerData playerTime = playerDataMap.get(player);
        if ((System.currentTimeMillis() - playerTime.timeStamp) / 1000 > playerTime.allottedTime) {
            player.sendMessage(Text.literal("INVALID ENTRIS TIMESTAMP"));
            ServerNetworkHelper.send(player, new ValidEntrisScorePayload(-1));
            playerDataMap.remove(player);
        } else {
            ServerNetworkHelper.send(player, new ValidEntrisScorePayload(score));
            playerDataMap.get(player).score = score;
        }
    }
}

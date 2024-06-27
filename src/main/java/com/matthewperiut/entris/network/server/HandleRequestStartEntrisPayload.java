package com.matthewperiut.entris.network.server;

import com.matthewperiut.entris.BookShelvesUtil;
import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.client.SlotEnabler;
import com.matthewperiut.entris.network.ServerNetworkHelper;
import com.matthewperiut.entris.network.payload.AllowEntrisPayload;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.matthewperiut.entris.Entris.MARGIN_OF_ERROR_TIME;
import static com.matthewperiut.entris.Entris.playerDataMap;
import static net.minecraft.util.math.MathHelper.ceil;

public class HandleRequestStartEntrisPayload {
    public static void handle(ServerPlayerEntity player, int requiredLevels) {
        int requiredLapis = ceil(requiredLevels / 10.f);

        if (player.isCreative()) {
            playerDataMap.put(player, new Entris.PlayerData(System.currentTimeMillis(), (requiredLevels * 6) + MARGIN_OF_ERROR_TIME));
            ServerNetworkHelper.send(player, new AllowEntrisPayload(true));
            return;
        }

        if (requiredLevels > 30) {
            ServerNetworkHelper.send(player, new AllowEntrisPayload(false));
            return;
        }

        int bookshelveMaxLevel = (int) (4 + (1.74* BookShelvesUtil.countBookShelves(player)));
        if (requiredLevels > bookshelveMaxLevel) {
            ServerNetworkHelper.send(player, new AllowEntrisPayload(false));
            return;
        }

        if (player.currentScreenHandler.getSlot(0).getStack().hasEnchantments()){
            ServerNetworkHelper.send(player, new AllowEntrisPayload(false));
            return;
        }
        if (!player.currentScreenHandler.getSlot(0).getStack().isEnchantable()){
            ServerNetworkHelper.send(player, new AllowEntrisPayload(false));
            return;
        }
        if (player.currentScreenHandler.getSlot(0).getStack().getItem() == Items.BOOK){
            ServerNetworkHelper.send(player, new AllowEntrisPayload(false));
            return;
        }

        if (requiredLapis > 0) {
            int lapisCt = ((EnchantmentScreenHandler)player.currentScreenHandler).getLapisCount();
            if (requiredLapis <= lapisCt) {
                if (player.experienceLevel >= requiredLevels) {
                    player.addExperienceLevels(-requiredLapis);
                    player.currentScreenHandler.getSlot(1).takeStack(requiredLapis);

                    // todo: check if the item is valid
                    ((SlotEnabler) player.currentScreenHandler.getSlot(0)).setCanTake(false);
                    ServerNetworkHelper.send(player, new AllowEntrisPayload(true));
                    playerDataMap.put(player, new Entris.PlayerData(System.currentTimeMillis(), (requiredLevels * 6) + MARGIN_OF_ERROR_TIME));
                    return;
                }
            }

            ServerNetworkHelper.send(player, new AllowEntrisPayload(false));
        }
    }
}

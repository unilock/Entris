package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.BookShelvesUtil;
import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.client.SlotEnabler;
import com.matthewperiut.entris.network.payload.AllowEntrisPayload;
import com.matthewperiut.entris.network.payload.FinishEntrisPayload;
import com.matthewperiut.entris.network.payload.RequestStartEntrisPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;

import java.util.HashMap;

import static com.matthewperiut.entris.BookShelvesUtil.countBookShelves;
import static net.minecraft.util.math.MathHelper.ceil;

public class EntrisFabric implements ModInitializer {

    public static final int MARGIN_OF_ERROR_TIME = 10;

    public static class PlayerTime {
        public long timeStamp;
        public int allottedTime;

        public PlayerTime(long timeStamp, int allottedTime) {
            this.timeStamp = timeStamp;
            this.allottedTime = allottedTime;
        }
    }
    public static HashMap<PlayerEntity, PlayerTime> playerTimes = new HashMap<>();

    @Override
    public void onInitialize() {
        Entris.init();
        PayloadTypeRegistry.playC2S().register(RequestStartEntrisPayload.ID, RequestStartEntrisPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AllowEntrisPayload.ID, AllowEntrisPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FinishEntrisPayload.ID, FinishEntrisPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestStartEntrisPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                int requiredLevels = payload.levels();
                int requiredLapis = ceil(requiredLevels / 10.f);

                if (requiredLevels > 30) {
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    return;
                }

                int bookshelveMaxLevel = (int) (4 + (1.74*BookShelvesUtil.countBookShelves(context.player())));
                if (payload.levels() > bookshelveMaxLevel) {
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    return;
                }

                if (context.player().isCreative()) {
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(true));
                    return;
                }

                if (requiredLapis > 0) {
                    int lapisCt = ((EnchantmentScreenHandler)context.player().currentScreenHandler).getLapisCount();
                    if (requiredLapis <= lapisCt) {
                        if (context.player().experienceLevel >= requiredLevels) {
                            context.player().addExperienceLevels(-requiredLapis);
                            context.player().currentScreenHandler.getSlot(1).takeStack(requiredLapis);

                            // todo: check if the item is valid
                            ((SlotEnabler) context.player().currentScreenHandler.getSlot(0)).setCanTake(false);
                            ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(true));
                            playerTimes.put(context.player(), new PlayerTime(System.currentTimeMillis(), (requiredLevels * 6) + MARGIN_OF_ERROR_TIME));
                            return;
                        }
                    }

                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(FinishEntrisPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                int score = payload.score();
                PlayerTime playerTime = playerTimes.get(context.player());
                if (!context.player().isCreative()) {
                    if ((System.currentTimeMillis() - playerTime.timeStamp) / 1000 > playerTime.allottedTime) {
                        context.player().sendMessage(Text.literal("INVALID ENTRIS TIMESTAMP"));
                    } else {
                        context.player().sendMessage(Text.literal("Good Job on " + score + "!!"));
                    }
                } else {
                    context.player().sendMessage(Text.literal("Good Job on " + score + "!!"));
                }

            });
        });
    }
}

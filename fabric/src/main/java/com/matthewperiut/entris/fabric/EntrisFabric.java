package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.BookShelvesUtil;
import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.client.SlotEnabler;
import com.matthewperiut.entris.enchantment.EnchantmentHelp;
import com.matthewperiut.entris.network.payload.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static net.minecraft.util.math.MathHelper.ceil;

public class EntrisFabric implements ModInitializer {

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

    @Override
    public void onInitialize() {
        Entris.init();
        PayloadTypeRegistry.playC2S().register(RequestStartEntrisPayload.ID, RequestStartEntrisPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AllowEntrisPayload.ID, AllowEntrisPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FinishEntrisPayload.ID, FinishEntrisPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ValidEntrisScorePayload.ID, ValidEntrisScorePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestEntrisEnchantsPayload.ID, RequestEntrisEnchantsPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestStartEntrisPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                int requiredLevels = payload.levels();
                int requiredLapis = ceil(requiredLevels / 10.f);

                if (context.player().isCreative()) {
                    playerDataMap.put(context.player(), new PlayerData(System.currentTimeMillis(), (requiredLevels * 6) + MARGIN_OF_ERROR_TIME));
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(true));
                    return;
                }

                if (requiredLevels > 30) {
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    return;
                }

                int bookshelveMaxLevel = (int) (4 + (1.74*BookShelvesUtil.countBookShelves(context.player())));
                if (payload.levels() > bookshelveMaxLevel) {
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    return;
                }

                if (context.player().currentScreenHandler.getSlot(0).getStack().hasEnchantments()){
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    return;
                }
                if (!context.player().currentScreenHandler.getSlot(0).getStack().isEnchantable()){
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    return;
                }
                if (context.player().currentScreenHandler.getSlot(0).getStack().getItem() == Items.BOOK){
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
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
                            playerDataMap.put(context.player(), new PlayerData(System.currentTimeMillis(), (requiredLevels * 6) + MARGIN_OF_ERROR_TIME));
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
                PlayerData playerTime = playerDataMap.get(context.player());
                if ((System.currentTimeMillis() - playerTime.timeStamp) / 1000 > playerTime.allottedTime) {
                    context.player().sendMessage(Text.literal("INVALID ENTRIS TIMESTAMP"));
                    ServerPlayNetworking.send(context.player(), new ValidEntrisScorePayload(-1));
                    playerDataMap.remove(context.player());
                } else {
                    ServerPlayNetworking.send(context.player(), new ValidEntrisScorePayload(score));
                    playerDataMap.get(context.player()).score = score;
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(RequestEntrisEnchantsPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ArrayList<Identifier> enchantments = new ArrayList<>();
                ArrayList<Integer> levels = new ArrayList<>();

                for (int i = 0; i < payload.enchants().size(); i++) {
                    String[] enchant = payload.enchants().get(i).split(" ");
                    System.out.println(enchant[0]);
                    enchantments.add(Identifier.of(enchant[0]));
                    levels.add(Integer.parseInt(enchant[1]));
                }

                int ct = 0;
                for (int l : levels) {
                    ct += l;
                }

                if (ct * 1000 > playerDataMap.get(context.player()).score) {
                    ServerPlayNetworking.send(context.player(), new AllowEntrisPayload(false));
                    playerDataMap.remove(context.player());
                } else {
                    // Apply enchants from enchantments arraylist
                    ItemStack stack = context.player().currentScreenHandler.getSlot(0).getStack();
                    for (int i = 0; i < enchantments.size(); i++) {
                        Identifier enchantment = enchantments.get(i);
                        int level = levels.get(i);

                        // why is modern mc like this?
                        Optional<RegistryEntry.Reference<Enchantment>> entry = context.server().getOverworld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(enchantment);
                        RegistryEntry<Enchantment> H = context.server().getOverworld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(entry.get().registryKey());

                        stack.addEnchantment(H, level);
                    }

                    context.player().currentScreenHandler.getSlot(0).setStack(stack);
                    ((SlotEnabler) context.player().currentScreenHandler.getSlot(0)).setCanTake(true);
                }
            });
        });
    }
}

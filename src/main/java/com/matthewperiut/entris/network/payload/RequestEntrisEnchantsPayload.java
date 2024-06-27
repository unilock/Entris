package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;

public record RequestEntrisEnchantsPayload(ArrayList<String> enchants) implements FabricPacket {
    public static final PacketType<RequestEntrisEnchantsPayload> TYPE = PacketType.create(EntrisNetworkingConstants.REQUEST_ENTRIS_ENCHANTS_PACKET_ID, RequestEntrisEnchantsPayload::new);

    public RequestEntrisEnchantsPayload(PacketByteBuf byteBuf) {
        this(new ArrayList<>());
        // Read the size of the list
        int size = byteBuf.readInt();
        for (int i = 0; i < size; i++) {
            // Read the length of the string
            int strLen = byteBuf.readInt();
            // Read the string with the specified length
            byte[] strBytes = new byte[strLen];
            byteBuf.readBytes(strBytes);
            enchants.add(new String(strBytes, java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    @Override
    public void write(PacketByteBuf byteBuf) {
        // Write the size of the list
        byteBuf.writeInt(enchants.size());
        for (String s : enchants) {
            byte[] strBytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            // Write the length of the string
            byteBuf.writeInt(strBytes.length);
            // Write the string bytes
            byteBuf.writeBytes(strBytes);
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

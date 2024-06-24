package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.ArrayList;

public record RequestEntrisEnchantsPayload(ArrayList<String> enchants)  implements CustomPayload {
    public static final PacketCodec<ByteBuf, ArrayList<String>> PACKET_CODEC = new PacketCodec<ByteBuf, ArrayList<String>>() {
        @Override
        public ArrayList<String> decode(ByteBuf byteBuf) {
            // Read the size of the list
            int size = byteBuf.readInt();
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                // Read the length of the string
                int strLen = byteBuf.readInt();
                // Read the string with the specified length
                byte[] strBytes = new byte[strLen];
                byteBuf.readBytes(strBytes);
                list.add(new String(strBytes, java.nio.charset.StandardCharsets.UTF_8));
            }
            return list;
        }

        @Override
        public void encode(ByteBuf byteBuf, ArrayList<String> list) {
            // Write the size of the list
            byteBuf.writeInt(list.size());
            for (String s : list) {
                byte[] strBytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                // Write the length of the string
                byteBuf.writeInt(strBytes.length);
                // Write the string bytes
                byteBuf.writeBytes(strBytes);
            }
        }
    };

    public static final Id<RequestEntrisEnchantsPayload> ID = new Id<>(EntrisNetworkingConstants.REQUEST_ENTRIS_ENCHANTS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, RequestEntrisEnchantsPayload> CODEC =
            PacketCodec.tuple(PACKET_CODEC,
                    RequestEntrisEnchantsPayload::enchants,
                    RequestEntrisEnchantsPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

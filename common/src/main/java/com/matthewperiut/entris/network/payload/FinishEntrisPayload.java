package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record FinishEntrisPayload(int score) implements CustomPayload {
    public static final PacketCodec<ByteBuf, Integer> PACKET_CODEC = new PacketCodec<ByteBuf, Integer>() {
        @Override
        public Integer decode(ByteBuf byteBuf) {
            // Read an integer from the ByteBuf
            return byteBuf.readInt();
        }

        @Override
        public void encode(ByteBuf byteBuf, Integer value) {
            // Write the integer to the ByteBuf
            byteBuf.writeInt(value);
        }
    };

    public static final Id<FinishEntrisPayload> ID = new Id<>(EntrisNetworkingConstants.FINISH_ENTRIS_GAME_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, FinishEntrisPayload> CODEC =
            PacketCodec.tuple(PACKET_CODEC,
                    FinishEntrisPayload::score,
                    FinishEntrisPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

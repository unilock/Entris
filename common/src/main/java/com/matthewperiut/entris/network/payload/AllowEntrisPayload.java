package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record AllowEntrisPayload(boolean allow)  implements CustomPayload {
    public static final PacketCodec<ByteBuf, Boolean> PACKET_CODEC = new PacketCodec<ByteBuf, Boolean>() {
        @Override
        public Boolean decode(ByteBuf byteBuf) {
            // Read an integer from the ByteBuf
            return byteBuf.readBoolean();
        }

        @Override
        public void encode(ByteBuf byteBuf, Boolean value) {
            // Write the integer to the ByteBuf
            byteBuf.writeBoolean(value);
        }
    };

    public static final Id<AllowEntrisPayload> ID = new Id<>(EntrisNetworkingConstants.START_ENTRIS_GAME_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, AllowEntrisPayload> CODEC =
            PacketCodec.tuple(PACKET_CODEC,
                    AllowEntrisPayload::allow,
                    AllowEntrisPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

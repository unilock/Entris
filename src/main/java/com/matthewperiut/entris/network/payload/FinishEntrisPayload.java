package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record FinishEntrisPayload(int score) implements FabricPacket {
    public static final PacketType<FinishEntrisPayload> TYPE = PacketType.create(EntrisNetworkingConstants.FINISH_ENTRIS_GAME_PACKET_ID, FinishEntrisPayload::new);

    public FinishEntrisPayload(PacketByteBuf byteBuf) {
        // Read an integer from the ByteBuf
        this(byteBuf.readInt());
    }

    @Override
    public void write(PacketByteBuf byteBuf) {
        // Write the integer to the ByteBuf
        byteBuf.writeInt(score);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

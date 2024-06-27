package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record ValidEntrisScorePayload(int score) implements FabricPacket {
    public static final PacketType<ValidEntrisScorePayload> TYPE = PacketType.create(EntrisNetworkingConstants.VALID_ENTRIS_SCORE_PACKET_ID, ValidEntrisScorePayload::new);

    public ValidEntrisScorePayload(PacketByteBuf byteBuf) {
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

package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record AllowEntrisPayload(boolean allow) implements FabricPacket {
    public static final PacketType<AllowEntrisPayload> TYPE = PacketType.create(EntrisNetworkingConstants.START_ENTRIS_GAME_PACKET_ID, AllowEntrisPayload::new);

    public AllowEntrisPayload(PacketByteBuf byteBuf) {
        // Read an integer from the ByteBuf
        this(byteBuf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf byteBuf) {
        // Write the integer to the ByteBuf
        byteBuf.writeBoolean(allow);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

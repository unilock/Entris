package com.matthewperiut.entris.network.payload;

import com.matthewperiut.entris.network.EntrisNetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record RequestStartEntrisPayload(int levels) implements FabricPacket {
    public static final PacketType<RequestStartEntrisPayload> TYPE = PacketType.create(EntrisNetworkingConstants.ALLOW_ENTRIS_GAME_PACKET_ID, RequestStartEntrisPayload::new);

    public RequestStartEntrisPayload(PacketByteBuf byteBuf) {
        // Read an integer from the ByteBuf
        this(byteBuf.readInt());
    }

    @Override
    public void write(PacketByteBuf byteBuf) {
        // Write the integer to the ByteBuf
        byteBuf.writeInt(levels);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

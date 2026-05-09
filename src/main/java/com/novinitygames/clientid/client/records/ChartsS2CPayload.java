package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record ChartsS2CPayload(Boolean val) implements FabricPacket {
    public static final Identifier CHARTS_PAYLOAD_ID = new Identifier(ClientID.MOD_ID, "charts");
    public static final PacketType<ChartsS2CPayload> ID = PacketType.create(CHARTS_PAYLOAD_ID, ChartsS2CPayload::new);

    public ChartsS2CPayload(PacketByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(val);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
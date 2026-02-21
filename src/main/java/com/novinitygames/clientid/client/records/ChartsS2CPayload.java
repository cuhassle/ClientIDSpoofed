package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChartsS2CPayload(Boolean val) implements CustomPayload {
    public static final Identifier CHARTS_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "charts");
    public static final Id<ChartsS2CPayload> ID = new Id<>(CHARTS_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ChartsS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, ChartsS2CPayload::val, ChartsS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
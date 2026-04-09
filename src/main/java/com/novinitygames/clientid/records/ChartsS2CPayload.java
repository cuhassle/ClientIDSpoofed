package com.novinitygames.clientid.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ChartsS2CPayload(Boolean val) implements CustomPacketPayload {
    public static final Identifier CHARTS_PAYLOAD_ID = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "charts");
    public static final CustomPacketPayload.Type<ChartsS2CPayload> ID = new CustomPacketPayload.Type<>(CHARTS_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChartsS2CPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ChartsS2CPayload::val, ChartsS2CPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
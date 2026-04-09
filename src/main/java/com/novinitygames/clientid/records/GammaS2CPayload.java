package com.novinitygames.clientid.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record GammaS2CPayload(float val) implements CustomPacketPayload {
    public static final Identifier GAMMA_PAYLOAD_ID = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "gamma");
    public static final CustomPacketPayload.Type<GammaS2CPayload> ID = new CustomPacketPayload.Type<>(GAMMA_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, GammaS2CPayload> CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, GammaS2CPayload::val, GammaS2CPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
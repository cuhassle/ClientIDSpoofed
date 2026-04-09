package com.novinitygames.clientid.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record VersionC2SPayload(String version) implements CustomPacketPayload {
    public static final Identifier MOD_LIST_PAYLOAD_ID = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "clientversion");
    public static final CustomPacketPayload.Type<VersionC2SPayload> ID = new CustomPacketPayload.Type<>(MOD_LIST_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, VersionC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, VersionC2SPayload::version, VersionC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
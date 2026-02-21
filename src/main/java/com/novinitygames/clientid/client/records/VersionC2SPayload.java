package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record VersionC2SPayload(String version) implements CustomPayload {
    public static final Identifier MOD_LIST_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "clientversion");
    public static final Id<VersionC2SPayload> ID = new Id<>(MOD_LIST_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, VersionC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, VersionC2SPayload::version, VersionC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
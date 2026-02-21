package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PackListC2SPayload(String list) implements CustomPayload {
    public static final Identifier PACK_LIST_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "packlist");
    public static final Id<PackListC2SPayload> ID = new Id<>(PACK_LIST_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, PackListC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, PackListC2SPayload::list, PackListC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
package com.novinitygames.clientid.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PackListC2SPayload(String list) implements CustomPacketPayload {
    public static final Identifier PACK_LIST_PAYLOAD_ID = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "packlist");
    public static final CustomPacketPayload.Type<PackListC2SPayload> ID = new CustomPacketPayload.Type<>(PACK_LIST_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PackListC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, PackListC2SPayload::list, PackListC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
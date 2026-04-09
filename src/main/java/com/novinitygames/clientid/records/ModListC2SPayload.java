package com.novinitygames.clientid.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ModListC2SPayload(String list) implements CustomPacketPayload {
    public static final Identifier MOD_LIST_PAYLOAD_ID = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "modlist");
    public static final CustomPacketPayload.Type<ModListC2SPayload> ID = new CustomPacketPayload.Type<>(MOD_LIST_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModListC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ModListC2SPayload::list, ModListC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
package com.novinitygames.clientid.records;

import com.mojang.serialization.Codec;
import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ModCheckC2SPayload(String uuid) implements CustomPacketPayload {
    public static final Identifier MOD_CHECK_PAYLOAD_ID = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "modcheck");
    public static final CustomPacketPayload.Type<ModCheckC2SPayload> ID = new CustomPacketPayload.Type<>(MOD_CHECK_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModCheckC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ModCheckC2SPayload::uuid, ModCheckC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
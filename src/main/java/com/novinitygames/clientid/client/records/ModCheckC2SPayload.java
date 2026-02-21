package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ModCheckC2SPayload(String uuid) implements CustomPayload {
    public static final Identifier MOD_CHECK_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "modcheck");
    public static final CustomPayload.Id<ModCheckC2SPayload> ID = new CustomPayload.Id<>(MOD_CHECK_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ModCheckC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ModCheckC2SPayload::uuid, ModCheckC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
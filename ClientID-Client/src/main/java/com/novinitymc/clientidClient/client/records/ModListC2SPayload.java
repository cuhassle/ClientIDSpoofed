package com.novinitymc.clientidClient.client.records;

import com.novinitymc.clientidClient.ClientidClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ModListC2SPayload(String list) implements CustomPayload {
    public static final Identifier MOD_LIST_PAYLOAD_ID = Identifier.of(ClientidClient.MOD_ID, "modlist");
    public static final CustomPayload.Id<ModListC2SPayload> ID = new CustomPayload.Id<>(MOD_LIST_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ModListC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ModListC2SPayload::list, ModListC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
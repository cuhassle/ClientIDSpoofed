package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record ModCheckC2SPayload(String uuid) implements FabricPacket {
    public static final Identifier MOD_CHECK_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "modcheck");
    public static final PacketType<ModCheckC2SPayload> ID = PacketType.create(MOD_CHECK_PAYLOAD_ID, ModCheckC2SPayload::new);

    public ModCheckC2SPayload(PacketByteBuf buf) {
        this(buf.readString(32767));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(uuid);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
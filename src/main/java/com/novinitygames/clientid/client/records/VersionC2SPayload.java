package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record VersionC2SPayload(String version) implements FabricPacket {
    public static final Identifier MOD_LIST_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "clientversion");
    public static final PacketType<VersionC2SPayload> ID = PacketType.create(MOD_LIST_PAYLOAD_ID, VersionC2SPayload::new);

    public VersionC2SPayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(version);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record ModListC2SPayload(String list) implements FabricPacket {
    public static final Identifier MOD_LIST_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "modlist");
    public static final PacketType<ModListC2SPayload> ID = PacketType.create(MOD_LIST_PAYLOAD_ID, ModListC2SPayload::new);

    public ModListC2SPayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(list);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
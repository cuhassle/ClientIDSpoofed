package com.novinitygames.clientid.client.records;

import com.novinitygames.clientid.ClientID;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record PackListC2SPayload(String list) implements FabricPacket {
    public static final Identifier PACK_LIST_PAYLOAD_ID = Identifier.of(ClientID.MOD_ID, "packlist");
    public static final PacketType<PackListC2SPayload> ID = PacketType.create(PACK_LIST_PAYLOAD_ID, PackListC2SPayload::new);

    public PackListC2SPayload(PacketByteBuf buf) {
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
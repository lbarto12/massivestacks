package lbarto12.stacks.controls;

import lbarto12.stacks.MassiveStacks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record HandshakePayload(int maxStackSize) implements CustomPacketPayload {
    public static final Type<HandshakePayload> PACKET_TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(MassiveStacks.MOD_ID, "handshake"));

    public static final StreamCodec<FriendlyByteBuf, HandshakePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, HandshakePayload::maxStackSize,
            HandshakePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}

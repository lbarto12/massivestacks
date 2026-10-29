package lbarto12.stacks.controls;

import lbarto12.stacks.MassiveStacks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

// custom payload for the server to handle selecting a specific, finite amount of a resource from a slot
public record TakeAmountPayload(int containerID, int slot, int amount) implements CustomPacketPayload {
    public static final Type<TakeAmountPayload> PACKET_TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(MassiveStacks.MOD_ID, "take_amount"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TakeAmountPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, TakeAmountPayload::containerID,
            ByteBufCodecs.VAR_INT, TakeAmountPayload::slot,
            ByteBufCodecs.VAR_INT, TakeAmountPayload::amount,
            TakeAmountPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}

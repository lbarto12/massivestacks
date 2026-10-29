package lbarto12.stacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lbarto12.stacks.controls.TakeAmountHandler;
import lbarto12.stacks.controls.TakeAmountPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class MassiveStacks implements ModInitializer {
    public static final String MOD_ID = "massivestacks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.serverboundPlay().register(TakeAmountPayload.PACKET_TYPE, TakeAmountPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TakeAmountPayload.PACKET_TYPE, ((payload, context) -> {
            TakeAmountHandler.handle(context.player(), payload);
        }));
    }

}

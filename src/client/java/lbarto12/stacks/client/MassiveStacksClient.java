package lbarto12.stacks.client;

import lbarto12.stacks.config.StackLimit;
import lbarto12.stacks.controls.HandshakePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class MassiveStacksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientConfigurationNetworking.registerGlobalReceiver(HandshakePayload.PACKET_TYPE, (payload, context) -> {
            if (!context.client().hasSingleplayerServer()) {
                StackLimit.setServer(payload.maxStackSize());
            }
        });
        ClientConfigurationConnectionEvents.DISCONNECT.register((handler, client) -> StackLimit.clearServer());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> StackLimit.clearServer());
    }
}

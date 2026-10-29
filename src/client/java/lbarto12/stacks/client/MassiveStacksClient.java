package lbarto12.stacks.client;

import lbarto12.stacks.controls.HandshakePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;

public class MassiveStacksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientConfigurationNetworking.registerGlobalReceiver(HandshakePayload.PACKET_TYPE, (payload, context) -> {

        });
    }
}

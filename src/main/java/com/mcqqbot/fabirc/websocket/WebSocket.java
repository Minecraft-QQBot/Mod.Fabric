package com.mcqqbot.fabirc.websocket;

import com.mcqqbot.fabirc.websocket.packets.CPacket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class WebSocket extends WebSocketClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("minecraft-qqbot");

    public WebSocket(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        LOGGER.info("From server:" + new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8));
        CPacket packet = new CPacket(message);
        packet.run().run();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.info("disconnected to server");
    }

    @Override
    public void onError(Exception ex) {

    }
}

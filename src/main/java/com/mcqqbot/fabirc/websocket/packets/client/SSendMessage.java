package com.mcqqbot.fabirc.websocket.packets.client;

public class SSendMessage extends SPacket{

    public SSendMessage(boolean success, String[] data) {
        super(success, data);
    }
}

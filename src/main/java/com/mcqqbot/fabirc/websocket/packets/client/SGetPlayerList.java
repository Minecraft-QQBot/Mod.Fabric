package com.mcqqbot.fabirc.websocket.packets.client;

public class SGetPlayerList extends SPacket{

    public SGetPlayerList(boolean success, String[] data) {
        super(success, data);
    }
}

package com.mcqqbot.fabirc.websocket.packets;

public class SGetPlayerList extends SPacket{

    public SGetPlayerList(boolean success, String[] data) {
        super(success, data);
    }
}

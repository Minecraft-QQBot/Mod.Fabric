package com.mcqqbot.fabirc.websocket.packets.client;
public class SExecuteCommand extends SPacket {
    public SExecuteCommand(boolean success, String[] data) {
        super(success, data);
    }
}

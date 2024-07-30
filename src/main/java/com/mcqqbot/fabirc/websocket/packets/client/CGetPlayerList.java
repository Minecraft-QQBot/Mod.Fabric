package com.mcqqbot.fabirc.websocket.packets.client;

import com.mcqqbot.fabirc.MinecraftQQBot;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class CGetPlayerList extends CPacket{
    public CGetPlayerList(String json) {
        super(json);
    }
    @Override
    public SPacket run() {
        LOGGER.info("GetPlayerList");
        PlayerManager playerManager = MinecraftQQBot.minecraftServer.getPlayerManager();
        List<ServerPlayerEntity> list = playerManager.getPlayerList();
        String[] data = new String[list.size()];
        for(int i = 0; i < list.size(); i++) {
            ServerPlayerEntity serverPlayerEntity = list.get(i);
            data[i] = serverPlayerEntity.getDisplayName().getString();
        }
        return new SGetPlayerList(true, data);
    }
}

package com.mcqqbot.fabirc.websocket.packets.client;

import com.mcqqbot.fabirc.MinecraftQQBot;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CSendMessage extends CPacket{
    public CSendMessage(String json) {
        super(json);
    }
    @Override
    public SPacket run() {
        String name = this.data[0];
        String message = this.data[1];
        ServerPlayerEntity player = MinecraftQQBot.minecraftServer.getPlayerManager().getPlayer(name);
        if(player != null) player.sendMessage(Text.of(message));
        return new SSendMessage(player != null, new String[]{""});
    }
}

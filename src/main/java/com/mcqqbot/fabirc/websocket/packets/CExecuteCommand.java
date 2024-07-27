package com.mcqqbot.fabirc.websocket.packets;

import com.mcqqbot.fabirc.MinecraftQQBot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

public class CExecuteCommand extends CPacket {
    public String type;
    public String[] data;
    public CExecuteCommand(String json) {
        super(json);
    }
    @Override
    public SExecuteCommand run() {
        LOGGER.info("Execute command:" + data[0]);
        String command = data[0];
        if(command.charAt(0) == '/')
            command = command.substring(1);
        MinecraftServer minecraftServer; //how can I get it
        CommandManager commandManager = Objects.requireNonNull(MinecraftQQBot.minecraftServer).getCommandManager();
        ServerCommandSource commandSource = MinecraftQQBot.minecraftServer.getCommandSource();
        commandManager.executeWithPrefix(commandSource, command);
        return new SExecuteCommand(true,
                new String[]{""});
    }
}

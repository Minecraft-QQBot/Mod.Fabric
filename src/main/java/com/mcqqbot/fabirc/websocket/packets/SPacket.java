package com.mcqqbot.fabirc.websocket.packets;

import com.google.gson.Gson;
import com.mcqqbot.fabirc.MinecraftQQBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SPacket {
    public boolean success;
    public String[] data;
    Gson gson = new Gson();
    public static final Logger LOGGER = LoggerFactory.getLogger("minecraft-qqbot");

    public SPacket(boolean success, String[] data) {
        this.success = success;
        this.data = data;
    }

    public boolean run() {
        MinecraftQQBot.SwebSocket.send(Base64.getEncoder().encodeToString(gson.toJson(this).getBytes(StandardCharsets.UTF_8)));
        return true;
    }

    public boolean isSuccess() {
        return success;
    }

    public String[] getData() {
        return data;
    }

}

package com.mcqqbot.fabirc.websocket.packets.client;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CPacket {
    public String type;
    public String[] data;
    Gson gson = new Gson();
    public static final Logger LOGGER = LoggerFactory.getLogger("minecraft-qqbot");

    public CPacket(String json) {
        try {
            json = new String(Base64.getDecoder().decode(json), StandardCharsets.UTF_8);
            type = gson.fromJson(json, this.getClass()).getType();
            data = gson.fromJson(json, this.getClass()).getData();
        } catch (Exception e) {
            LOGGER.error("\"" + json + "\" is not a valid JSON string");
        }
    }
    public CPacket(String type, String[] data) {
        this.type = type;
        this.data = data;
    }

    protected CPacket() {
    }

    public SPacket run() {
        switch (this.getType()) {
            case "command":
                CExecuteCommand executeCommandPacket = new CExecuteCommand(gson.toJson(this));
                return executeCommandPacket.run();
            case "message":
                CSendMessage sendMessagePacket = new CSendMessage(gson.toJson(this));
            case "player_list":
                CGetPlayerList playerListPacket = new CGetPlayerList(gson.toJson(this));

        }
        return null;
    }
    public String getType() {
        return type;
    }

    public String[] getData() {
        return data;
    }

}

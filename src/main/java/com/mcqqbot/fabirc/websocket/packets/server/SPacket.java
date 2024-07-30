package com.mcqqbot.fabirc.websocket.packets.server;

import com.google.gson.Gson;
import com.mcqqbot.fabirc.websocket.packets.client.CExecuteCommand;
import com.mcqqbot.fabirc.websocket.packets.client.CGetPlayerList;
import com.mcqqbot.fabirc.websocket.packets.client.CSendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SPacket {
    public String type;
    public String[] data;
    Gson gson = new Gson();
    public static final Logger LOGGER = LoggerFactory.getLogger("minecraft-qqbot");

    public SPacket(String json) {
        try {
            json = new String(Base64.getDecoder().decode(json), StandardCharsets.UTF_8);
            type = gson.fromJson(json, this.getClass()).getType();
            data = gson.fromJson(json, this.getClass()).getData();
        } catch (Exception e) {
            LOGGER.error("\"" + json + "\" is not a valid JSON string");
        }
    }
    public SPacket(String type, String[] data) {
        this.type = type;
        this.data = data;
    }

    public com.mcqqbot.fabirc.websocket.packets.client.SPacket run() {
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

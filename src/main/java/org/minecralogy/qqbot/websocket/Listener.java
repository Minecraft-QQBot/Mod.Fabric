package org.minecralogy.qqbot.websocket;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.minecralogy.qqbot.BotCommandSource;
import org.minecralogy.qqbot.Utils;
import org.slf4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.minecralogy.qqbot.Bot;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Listener extends WebSocketClient {
    private static final Logger LOGGER = LogUtils.getLogger();
    Timer task;
    public Listener(URI serverUri) {
        super(serverUri);
    }
    public Listener(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
        HashMap<String, String> headers = new HashMap<>();
        headers.put("name", Bot.config.getName());
        headers.put("token", Bot.config.getToken());
        this.addHeader("type", "Fabric");
        this.addHeader("info", Utils.encode(headers));

    }
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Listener:Connected to server");
        task.cancel();
    }

    @Override
    public void onMessage(String s) {
        String msg = new String(s.getBytes(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        msg = Utils.decode(msg);
        if(!Bot.isValidStrictly(msg, Package.class))
        {
            LOGGER.warn("Not valid message");
            return;
        }
        Package message = gson.fromJson(msg, Package.class);
        Object response = null;
        HashMap<String, Object> responseMessage = new HashMap<>();
        boolean success = false;
        switch (message.type) {
            case "command":
                MinecraftServer server = Bot.server;
                BotCommandSource botCommandSource = new BotCommandSource(
                        server, server.getOverworld() == null ? Vec3d.ZERO : Vec3d.of(server.getOverworld().getSpawnPos()), Vec2f.ZERO, server.getOverworld(), 4, "Server", Text.literal("Server"), server, null
                );
                server.getCommandManager().execute(server.getCommandManager().getDispatcher().parse(message.data, botCommandSource), message.data);
                response = botCommandSource.getResult();
                success = botCommandSource.isSuccess();
                break;
            case "player_list":
                List<ServerPlayerEntity> l = Bot.server.getPlayerManager().getPlayerList();
                List<String> res = new ArrayList<String>();
                for(ServerPlayerEntity spe : l) {
                    res.add(spe.getName().getString());
                }
                response = res;
            case "message":
                Bot.server.sendMessage(Text.of(message.data));
                success = true;
                break;
            default:
                LOGGER.warn("Unknow package from bot:{}", msg);
        }
        responseMessage.put("success", success);
        responseMessage.put("data", response);
        this.send(Utils.encode(responseMessage));
        LOGGER.info("Send {} to bot", responseMessage);

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LOGGER.info("Listener:Disconnected to server");
        ReConnectedTask reConnectedTask = new ReConnectedTask();
        reConnectedTask.listener = this;
        task.schedule(reConnectedTask, 0, Bot.config.getReconnect_interval()* 1000L);
    }

    @Override
    public void onError(Exception e) {

    }
    private static class ReConnectedTask extends TimerTask {
        Listener listener;
        @Override
        public void run() {
            try {
                listener.connectBlocking();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

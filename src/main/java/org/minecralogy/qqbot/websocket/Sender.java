package org.minecralogy.qqbot.websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.minecralogy.qqbot.Bot;
import org.minecralogy.qqbot.Utils;
import org.slf4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sender extends WebSocketClient {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private String message;
    Timer task;
    public Sender(URI serverUri) {
        super(serverUri);
    }
    public Sender(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
        HashMap<String, String> headers = new HashMap<>();
        headers.put("name", Bot.config.getName());
        headers.put("token", Bot.config.getToken());
        this.addHeader("info", Utils.encode(headers));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Sender:Connected to server");
        this.sendServerStartup();
        task.cancel();
    }

    @Override
    public void onMessage(String s) {
        this.lock.lock();
        try {
            this.message = s;
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LOGGER.info("Sender:Disconnected to server");
        ReConnectedTask reConnectedTask = new ReConnectedTask();
        reConnectedTask.sender = this;
        task.schedule(reConnectedTask, 0, Bot.config.getReconnect_interval()* 1000L);

    }

    @Override
    public void onError(Exception e) {

    }

    private static class ReConnectedTask extends TimerTask {
        Sender sender = null;
        @Override
        public void run() {
            try {
                sender.connectBlocking();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean sendData(String event_type, Object data, Boolean waitResponse) {
        boolean responseReceived = false;
        HashMap<String, Object> messageData = new HashMap<>();
        messageData.put("data", data);
        messageData.put("type", event_type);
        try {
            this.send(Utils.encode(messageData));
        } catch (WebsocketNotConnectedException error) {
            LOGGER.warn("Can't send data");
            return false;
        }
        if (!waitResponse) return true;
        this.lock.lock();
        try {
            responseReceived = this.condition.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
        } finally {
            this.lock.unlock();
        }
        if (!responseReceived) {
            LOGGER.warn("Wait response time out");
            return false;
        }
        HashMap<String, ?> hm = (new Gson().fromJson(Utils.decode(this.message), new TypeToken<HashMap<String, Object>>() {}.getType()));
        return (boolean) hm.get("success");
    }
    public void sendServerStartup() {
        HashMap<String, Object> data = new HashMap<>();
        if (this.sendData("server_startup", data, true)) LOGGER.info("Send {} message success", "server start");
        else LOGGER.warn("Can't send {} message", "server start");
    }

    public void sendServerShutdown() {
        HashMap<String, Object> data = new HashMap<>();
        if (this.sendData("server_shutdown", data, true)) LOGGER.info("Send {} message success", "server stop");
        else LOGGER.warn("Can't send {} message", "server stop");
    }

    public void sendPlayerLeft(String name) {
        if (this.sendData("player_left", name, true)) LOGGER.info("Send {} message success", "player left");
        else LOGGER.warn("Can't send {} message", "player left");
    }

    public void sendPlayerJoined(String name) {
        if (this.sendData("player_joined", name, true)) LOGGER.info("Send {} message success", "player joined");
        else LOGGER.warn("Can't send {} message", "player joined");
    }

    public void sendPlayerChat(String name, String message) {
        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(message);
        this.sendData("player_chat", data, false);
        LOGGER.info("Send {} message success", "player");
    }

    public void sendPlayerDeath(String name, String message) {
        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(message);
        if (this.sendData("player_death", data, true)) LOGGER.info("Send {} message success", "player death");
        else LOGGER.warn("Can't send {} message", "player death");
    }

    public boolean sendSynchronousMessage(String message) {
        return this.sendData("message", message, true);
    }
}

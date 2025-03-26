package org.minecralogy.qqbot;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.minecralogy.qqbot.command.QQCommand;
import org.minecralogy.qqbot.websocket.Listener;
import org.minecralogy.qqbot.websocket.Sender;
import org.slf4j.Logger;
import java.io.*;
import java.net.URISyntaxException;

public class Bot implements ModInitializer {
    public static Config config;
    public static MinecraftServer server = null;
    public static Listener listener;
    public static Sender sender;
    static final Logger LOGGER = LogUtils.getLogger();
    public static <T> boolean isValidStrictly(String json, Class<T> type) {
        TypeAdapter<T> strictAdapter = new Gson().getAdapter(type);
        try {
            strictAdapter.fromJson(json);
        } catch (JsonSyntaxException | IOException e) {
            return false;
        }
        return true;
    }
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            QQCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            server = minecraftServer;
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((minecraftServer) -> {
            Bot.sender.sendServerShutdown();
        });
        String path = FabricLoader.getInstance().getConfigDir().toAbsolutePath().toString();
        path = path + "\\qq_bot.json";
        LOGGER.info("The bot config:" + path);
        File file = new File(path);
        if(!file.canRead() || file.isDirectory()) {
            LOGGER.error("The configuration file does not exist");
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Can't create configuration file");
                throw new RuntimeException(e);
            }
            System.exit(0);
        }
        Gson g = new Gson();
        InputStream inputStream;
        String conf;
        try {
            inputStream = new FileInputStream(file);
            conf = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            LOGGER.error("Can't read configuration file");
            System.exit(0);
            throw new RuntimeException(e);
        }
        if(!isValidStrictly(conf, Config.class)) {
            LOGGER.error("Configuration file is not a json file");
            System.exit(0);
        }
        Gson gson = new Gson();
        config = gson.fromJson(conf, Config.class);
        if(config.uri.endsWith("/")) config.uri = config.uri.substring(0, config.uri.length() -1 );
        try {
            listener = new Listener(config.uri + "/websocket/minecraft");
            sender = new Sender(config.uri + "/websocket/bot");
        } catch (URISyntaxException e) {
            LOGGER.error("Wrong uri");
            System.exit(0);
            throw new RuntimeException(e);
        }
    }
}

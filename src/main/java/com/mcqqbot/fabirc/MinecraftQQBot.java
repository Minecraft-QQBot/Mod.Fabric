package com.mcqqbot.fabirc;

import com.google.gson.Gson;
import com.mcqqbot.fabirc.websocket.LWebSocket;
import com.mcqqbot.fabirc.websocket.SWebSocket;
import com.mcqqbot.fabirc.websocket.packets.server.SPlayerJoin;
import com.mcqqbot.fabirc.websocket.packets.server.SServerStart;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MinecraftQQBot implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("minecraft-qqbot");
	public static LWebSocket lwebSocketC;
	public static SWebSocket SwebSocket;
	public static MinecraftServer minecraftServer;
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//Read Config
		Config config;
		Gson gson = new Gson();
		File config_file = new File(FabricLoader.getInstance().getConfigDir().toString() + "/qq_bot.json");
		if (!FabricLoader.getInstance().getConfigDir().toFile().exists())
			if(!FabricLoader.getInstance().getConfigDir().toFile().mkdirs()) {
				LOGGER.error("Can't mkdir");
				System.exit(0);
			}
		try {
			if (!config_file.exists()) {
				LOGGER.warn("You need put config at {GameDir}/config/qq_bot.json");
				return;
			} else {
				config = gson.fromJson(Files.readString(config_file.toPath()), Config.class);
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
		Map<String, String> header = new HashMap<>();
		H h = new H();
		h.name = config.name;
		h.token = config.token;
		header.put("info", Base64.getEncoder().encodeToString(gson.toJson(h).getBytes(StandardCharsets.UTF_8)));
		LOGGER.info("Header is \"" + gson.toJson(h) + "\"");
		LOGGER.info(Base64.getEncoder().encodeToString(gson.toJson(h).getBytes(StandardCharsets.UTF_8)));
//		header.put("token", config.token);
//		header.put("name", config.name);
		try {
			lwebSocketC = new LWebSocket(new URI(config.url + "/websocket/minecraft"), header);
			lwebSocketC.connect();
			SwebSocket = new SWebSocket(new URI(config.url + "/websocket/bot"), header);
			SwebSocket.connect();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		new SServerStart("server_startup", new String[]{""}).run();
	}
}
class H {
	String token;
	String name;
}
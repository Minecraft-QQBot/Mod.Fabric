package org.minecralogy.qqbot;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.Formatting;
import org.minecralogy.qqbot.websocket.Package;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private static final HashMap<String, Formatting> mapping = new HashMap<>();

    private static final Gson gson = new Gson();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Type type = new TypeToken<HashMap<String, Object>>() {}.getType();

    public Utils() {
        mapping.put("black", Formatting.BLACK);
        mapping.put("dark_blue", Formatting.DARK_BLUE);
        mapping.put("dark_green", Formatting.DARK_GREEN);
        mapping.put("dark_aqua", Formatting.DARK_AQUA);
        mapping.put("dark_red", Formatting.DARK_RED);
        mapping.put("dark_purple", Formatting.DARK_PURPLE);
        mapping.put("gold", Formatting.GOLD);
        mapping.put("gray", Formatting.GRAY);
        mapping.put("dark_gray", Formatting.DARK_GRAY);
        mapping.put("blue", Formatting.BLUE);
        mapping.put("green", Formatting.GREEN);
        mapping.put("aqua", Formatting.AQUA);
        mapping.put("red", Formatting.RED);
        mapping.put("light_purple", Formatting.LIGHT_PURPLE);
        mapping.put("yellow", Formatting.YELLOW);
        mapping.put("white", Formatting.WHITE);
    }

    public static String encode(HashMap<String, ?> originalMap) {
        String string = gson.toJson(originalMap);
        return encoder.encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String original) {
        byte[] stringBytes = decoder.decode(original.getBytes(StandardCharsets.UTF_8));
        String decodeString = new String(stringBytes, StandardCharsets.UTF_8);
        return decodeString;
    }

    public String toStringMessage(List<LinkedTreeMap<String, String>> original) {
        StringBuilder message = new StringBuilder();
        for (LinkedTreeMap<String, String> section : original) {
            message.append(mapping.getOrDefault(section.get("color"), Formatting.GRAY)).append(section.get("text"));
        }
        return message.toString();
    }
}

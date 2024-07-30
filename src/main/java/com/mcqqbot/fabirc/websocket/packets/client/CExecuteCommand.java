package com.mcqqbot.fabirc.websocket.packets.client;

import com.mcqqbot.fabirc.MinecraftQQBot;
import com.mojang.brigadier.ResultConsumer;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.message.SignedCommandArguments;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.FutureQueue;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

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
        CommandManager commandManager = Objects.requireNonNull(MinecraftQQBot.minecraftServer).getCommandManager();
        //Test commandSource = (Test) MinecraftQQBot.minecraftServer.getCommandSource();
        commandManager.executeWithPrefix(MinecraftQQBot.minecraftServer.getCommandSource(), command);
        return new SExecuteCommand(true, new String[]{""});
                //commandSource.back.toArray(new String[0]));
    }
}
/*
class Test extends ServerCommandSource {
    List<String> back = new ArrayList<>();

    public Test(CommandOutput output, Vec3d pos, Vec2f rot, ServerWorld world, int level, String name, Text displayName, MinecraftServer server, @Nullable Entity entity) {
        super(output, pos, rot, world, level, name, displayName, server, entity);
    }

    protected Test(CommandOutput output, Vec3d pos, Vec2f rot, ServerWorld world, int level, String name, Text displayName, MinecraftServer server, @Nullable Entity entity, boolean silent, @Nullable ResultConsumer<ServerCommandSource> consumer, EntityAnchorArgumentType.EntityAnchor entityAnchor, SignedCommandArguments signedArguments, FutureQueue messageChainTaskQueue, IntConsumer returnValueConsumer) {
        super(output, pos, rot, world, level, name, displayName, server, entity, silent, consumer, entityAnchor, signedArguments, messageChainTaskQueue, returnValueConsumer);
    }
    @Override
    public void sendFeedback(Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        // 绕过Java权限检测
        Object output = null;
        Object silent = false;
        Method m;
        try {
            Field sourceField = null;
            m = ServerCommandSource.class.getDeclaredMethod("sendToOps", Text.class);
            m.setAccessible(true);
            sourceField = ServerCommandSource.class.getDeclaredField("output");
            sourceField = ServerCommandSource.class.getDeclaredField("silent");
            sourceField.setAccessible(true); // 绕过权限检测！
            output = sourceField.get(output);
            silent = sourceField.get(silent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        back.add(feedbackSupplier.get().getString());
        boolean bl = ((CommandOutput)output).shouldReceiveFeedback() && !((Boolean)silent);
        boolean bl2 = broadcastToOps && ((CommandOutput)output).shouldBroadcastConsoleToOps() && !((Boolean)silent);
        if (bl || bl2) {
            Text text = feedbackSupplier.get();
            if (bl) {
                ((CommandOutput)output).sendMessage(text);
            }

            if (bl2) {
                try {
                    m.invoke(ServerCommandSource.class.getDeclaredConstructor().newInstance(), text);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
*/
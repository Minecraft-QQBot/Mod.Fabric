package org.minecralogy.qqbot;

import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedCommandArguments;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.FutureQueue;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public class BotCommandSource extends ServerCommandSource {
    String result = "";

    public String getResult() {
        return result.substring(0, result.length()-1);
    }
    boolean success = true;

    public boolean isSuccess() {
        return success;
    }

    CommandOutput commandOutput;
    public BotCommandSource(CommandOutput output, Vec3d pos, Vec2f rot, ServerWorld world, int level, String name, Text displayName, MinecraftServer server, @Nullable Entity entity) {
        super(output, pos, rot, world, level, name, displayName, server, entity);
        commandOutput = output;
    }

    protected BotCommandSource(CommandOutput output, Vec3d pos, Vec2f rot, ServerWorld world, int level, String name, Text displayName, MinecraftServer server, @Nullable Entity entity, boolean silent, ReturnValueConsumer resultStorer, EntityAnchorArgumentType.EntityAnchor entityAnchor, SignedCommandArguments signedArguments, FutureQueue messageChainTaskQueue) {
        super(output, pos, rot, world, level, name, displayName, server, entity, silent, resultStorer, entityAnchor, signedArguments, messageChainTaskQueue);
        commandOutput = output;
    }

    @Override
    public void sendChatMessage(SentMessage message, boolean filterMaskEnabled, MessageType.Parameters params) {
        if (!this.isSilent()) {
            ServerPlayerEntity serverPlayerEntity = this.getPlayer();
            if (serverPlayerEntity != null) {
                serverPlayerEntity.sendChatMessage(message, filterMaskEnabled, params);
            } else {
                this.getOutput().sendMessage(params.applyChatDecoration(message.content()));
            }
        }
    }

    @Override
    public void sendMessage(Text message) {
        result += message.getString() + "\n";
        if (!this.isSilent()) {
            ServerPlayerEntity serverPlayerEntity = this.getPlayer();
            if (serverPlayerEntity != null) {
                serverPlayerEntity.sendMessage(message);
            } else {
                this.getOutput().sendMessage(message);
            }
        }
    }

    @Override
    public void sendFeedback(Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        boolean bl = this.getOutput().shouldReceiveFeedback() && !this.isSilent();
        boolean bl2 = broadcastToOps && this.getOutput().shouldBroadcastConsoleToOps() && !this.isSilent();
        Text text = (Text)feedbackSupplier.get();
        result += text.getString() + "\n";
        if (bl || bl2) {
            if (bl) {
                this.getOutput().sendMessage(text);
            }

            if (bl2) {
                this.sendToOp(text);
            }
        }
    }

    @Override
    public void sendError(Text message) {
        success = false;
        result += message.getString() + "\n";
        if (this.getOutput().shouldTrackOutput() && !this.isSilent()) {
            this.getOutput().sendMessage(Text.empty().append(message).formatted(Formatting.RED));
        }
    }
    private void sendToOp(Text message) {
        Text text = Text.translatable("chat.type.admin", this.getDisplayName(), message).formatted(Formatting.GRAY, Formatting.ITALIC);
        if (this.getServer().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
            for (ServerPlayerEntity serverPlayerEntity : this.getServer().getPlayerManager().getPlayerList()) {
                if (serverPlayerEntity != this.getOutput() && this.getServer().getPlayerManager().isOperator(serverPlayerEntity.getGameProfile())) {
                    serverPlayerEntity.sendMessage(text);
                }
            }
        }

        if (this.getOutput() != this.getServer() && this.getServer().getGameRules().getBoolean(GameRules.LOG_ADMIN_COMMANDS)) {
            this.getServer().sendMessage(text);
        }
    }
    public CommandOutput getOutput() {
        return commandOutput;
    }
}

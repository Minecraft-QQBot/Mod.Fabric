package org.minecralogy.qqbot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.minecralogy.qqbot.Bot;

import static net.minecraft.server.command.CommandManager.*;

public class QQCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("qq")
                .then(CommandManager.argument("message", MessageArgumentType.message()).executes(context -> {
                    MessageArgumentType.getSignedMessage(context, "message", message -> {
                        String m = SentMessage.of(message).content().getString();
                        String msg = String.format("[%s] <%s> %s", Bot.config.getName(), context.getSource().getName(), m);
                        Bot.sender.sendSynchronousMessage(msg);
                        System.out.println(msg);
                        CommandManager commandManager = context.getSource().getPlayer().getServer().getCommandManager();
                        commandManager.executeWithPrefix(context.getSource().getPlayer().getCommandSource(), "/say " + m);
                    });
                    return 1;
                }))
        );
    }
}

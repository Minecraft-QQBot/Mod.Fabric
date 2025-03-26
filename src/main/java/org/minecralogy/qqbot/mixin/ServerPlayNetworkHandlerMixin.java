package org.minecralogy.qqbot.mixin;

import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.minecralogy.qqbot.Bot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "onDisconnected", at = @At("HEAD"))
    void onDisconneted(DisconnectionInfo info, CallbackInfo ci) {
        Bot.sender.sendPlayerLeft(((ServerPlayNetworkHandler)(Object)this).player.getName().getString());
    }
    @Inject(method = "onChatMessage", at = @At("HEAD"))
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
       Bot.sender.sendPlayerChat(((ServerPlayNetworkHandler)(Object)this).player.getName().getString(), packet.chatMessage());
    }
}

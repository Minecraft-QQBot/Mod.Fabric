package com.mcqqbot.fabirc.mixin;

import com.mcqqbot.fabirc.MinecraftQQBot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	public abstract PlayerManager getPlayerManager();
	@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
        MinecraftQQBot.minecraftServer = getPlayerManager().getServer();
	}
}
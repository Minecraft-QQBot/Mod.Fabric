package com.mcqqbot.fabirc.mixin;

import com.mcqqbot.fabirc.websocket.packets.server.SPlayerChat;
import com.mcqqbot.fabirc.websocket.packets.server.SPlayerDeath;
import com.mcqqbot.fabirc.websocket.packets.server.SPlayerQuit;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "sendMessageToClient", at = @At("HEAD"))
    void sendMessageToClient(Text message, boolean overlay, CallbackInfo info) {
        new SPlayerChat("player_chat", new String[]{this.getEntityName(), message.getString()}).run();
    }
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        new SPlayerDeath("player_death", new String[]{this.getEntityName()}).run();
    }
    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnect(CallbackInfo info) {
        new SPlayerQuit("player_quit", new String[]{this.getEntityName()}).run();
    }
}

package org.minecralogy.qqbot.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.minecralogy.qqbot.Bot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))

    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        Bot.sender.sendPlayerDeath(((ServerPlayerEntity)(Object)this).getName().getString(), ((ServerPlayerEntity)(Object)this).getDamageTracker().getDeathMessage().getString());
    }
}

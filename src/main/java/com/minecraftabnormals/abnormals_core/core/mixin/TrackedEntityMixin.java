package com.minecraftabnormals.abnormals_core.core.mixin;

import com.minecraftabnormals.abnormals_core.core.events.EntityTrackingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.TrackedEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrackedEntity.class)
public final class TrackedEntityMixin {
	@Shadow
	@Final
	private Entity entity;

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;hasImpulse:Z", ordinal = 2, shift = At.Shift.AFTER), method = "sendChanges")
	private void update(CallbackInfo info) {
		EntityTrackingEvent.onEntityTracking(this.entity, true);
	}

	@Inject(at = @At("HEAD"), method = "sendChanges")
	private void tick(CallbackInfo info) {
		EntityTrackingEvent.onEntityTracking(this.entity, false);
	}
}

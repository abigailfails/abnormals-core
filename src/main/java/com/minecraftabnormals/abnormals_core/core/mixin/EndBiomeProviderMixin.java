package com.minecraftabnormals.abnormals_core.core.mixin;

import com.minecraftabnormals.abnormals_core.common.world.gen.ACLayerUtil;
import com.minecraftabnormals.abnormals_core.core.AbnormalsCore;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.layer.Layer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EndBiomeProvider.class)
public abstract class EndBiomeProviderMixin extends BiomeProvider {
	@Shadow
	@Final
	private Registry<Biome> biomes;
	@Shadow
	@Final
	private Biome highlands;
	@Shadow
	@Final
	private Biome midlands;
	@Shadow
	@Final
	private Biome barrens;

	private Layer noiseBiomeLayer;

	private EndBiomeProviderMixin(List<Biome> biomes) {
		super(biomes);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	private void init(Registry<Biome> lookupRegistry, long seed, CallbackInfo info) {
		this.noiseBiomeLayer = ACLayerUtil.createEndBiomeLayer(biomes, (seedModifier) -> new LazyAreaLayerContext(25, seed, seedModifier));
	}

	@Inject(at = @At("RETURN"), method = "getNoiseBiome(III)Lnet/minecraft/world/biome/Biome;", cancellable = true)
	private void addEndBiomes(int x, int y, int z, CallbackInfoReturnable<Biome> info) {
		Biome oldBiome = info.getReturnValue();
		if (oldBiome == this.highlands || oldBiome == this.midlands || oldBiome == this.barrens) {
			Biome newBiome = this.getNoiseBiome(x, z);
			if (newBiome != this.midlands) {
				info.setReturnValue(newBiome);
			}
		}
	}

	private Biome getNoiseBiome(int x, int z) {
		int biomeID = this.noiseBiomeLayer.area.get(x, z);
		Biome biome = this.biomes.byId(biomeID);
		if (biome == null) {
			AbnormalsCore.LOGGER.warn("Unknown end biome id: {}", biomeID);
			return this.midlands;
		}
		return biome;
	}
}

package com.minecraftabnormals.abnormals_core.client.renderer;

import com.minecraftabnormals.abnormals_core.client.RewardHandler;
import com.minecraftabnormals.abnormals_core.client.model.SlabfishHatModel;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.core.AbnormalsCore;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ocelot.sonar.client.util.OnlineImageCache;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import java.util.concurrent.TimeUnit;

public class SlabfishHatLayerRenderer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
	public static OnlineImageCache REWARD_CACHE = new OnlineImageCache(AbnormalsCore.MODID, 1, TimeUnit.DAYS);
	private final SlabfishHatModel model;

	public SlabfishHatLayerRenderer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer, SlabfishHatModel slabfishModel) {
		super(renderer);
		this.model = slabfishModel;
	}

	@Override
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, AbstractClientPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		RewardHandler.RewardProperties properties = RewardHandler.getRewardProperties();
		if (properties == null)
			return;

		RewardHandler.RewardProperties.SlabfishProperties slabfishProperties = properties.getSlabfishProperties();
		if (slabfishProperties == null)
			return;

		String defaultTypeUrl = slabfishProperties.getDefaultTypeUrl();
		IDataManager data = (IDataManager) entity;

		if (entity.isInvisible() || entity.isSpectator() || !(RewardHandler.SlabfishSetting.getSetting(data, RewardHandler.SlabfishSetting.ENABLED)) || defaultTypeUrl == null || !RewardHandler.REWARDS.containsKey(entity.getUUID()))
			return;

		RewardHandler.RewardData reward = RewardHandler.REWARDS.get(entity.getUUID());

		if (reward.getSlabfish() == null || reward.getTier() < 2)
			return;

		RewardHandler.RewardData.SlabfishData slabfish = reward.getSlabfish();
		ResourceLocation typeLocation = REWARD_CACHE.getTextureLocation(reward.getTier() >= 4 && slabfish.getTypeUrl() != null && RewardHandler.SlabfishSetting.getSetting(data, RewardHandler.SlabfishSetting.TYPE) ? slabfish.getTypeUrl() : defaultTypeUrl);
		if (typeLocation == null)
			return;
		
		ResourceLocation sweaterLocation = reward.getTier() >= 3 && slabfish.getSweaterUrl() != null && RewardHandler.SlabfishSetting.getSetting(data, RewardHandler.SlabfishSetting.SWEATER) ? REWARD_CACHE.getTextureLocation(slabfish.getSweaterUrl()) : null;
		ResourceLocation backpackLocation = slabfish.getBackpackUrl() != null && RewardHandler.SlabfishSetting.getSetting(data, RewardHandler.SlabfishSetting.BACKPACK) ? REWARD_CACHE.getTextureLocation(slabfish.getBackpackUrl()) : null;
		ModelRenderer body = this.model.body;
		ModelRenderer backpack = this.model.backpack;

		body.copyFrom(this.getParentModel().head);
		body.render(stack, buffer.getBuffer(slabfish.isTranslucent() ? RenderType.entityTranslucent(typeLocation) : RenderType.entityCutout(typeLocation)), packedLight, OverlayTexture.NO_OVERLAY);

		if (sweaterLocation != null)
			body.render(stack, buffer.getBuffer(RenderType.entityCutout(sweaterLocation)), packedLight, OverlayTexture.NO_OVERLAY);

		if (backpackLocation != null) {
			backpack.copyFrom(body);
			backpack.render(stack, buffer.getBuffer(RenderType.entityCutout(backpackLocation)), packedLight, OverlayTexture.NO_OVERLAY);
		}
	}
}

package com.minecraftabnormals.abnormals_core.core.events.listener;

import com.minecraftabnormals.abnormals_core.core.AbnormalsCore;
import com.minecraftabnormals.abnormals_core.core.api.IAgeableEntity;
import com.minecraftabnormals.abnormals_core.core.config.ACConfig;
import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abigailfails
 */
@Mod.EventBusSubscriber(modid = AbnormalsCore.MODID)
public final class ACEvents {
	public static final String POISON_TAG = AbnormalsCore.MODID + ":poisoned_by_potato";
	public static List<DataUtil.CustomNoteBlockInstrument> SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS = new ArrayList<>();

	@SubscribeEvent
	public static void onPlayerInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
		Entity target = event.getTarget();
		ItemStack stack = event.getItemStack();
		if (target instanceof IAgeableEntity && ((IAgeableEntity) target).hasGrowthProgress() && stack.getItem() == Items.POISONOUS_POTATO && ACConfig.ValuesHolder.isPoisonPotatoCompatEnabled() && ModList.get().isLoaded("quark")) {
			PlayerEntity player = event.getPlayer();
			CompoundNBT persistentData = target.getPersistentData();
			if (((IAgeableEntity) target).canAge(true) && !persistentData.getBoolean(POISON_TAG)) {
				if (target.level.random.nextDouble() < ACConfig.ValuesHolder.poisonEffectChance()) {
					target.playSound(SoundEvents.GENERIC_EAT, 0.5f, 0.25f);
					persistentData.putBoolean(POISON_TAG, true);
					if (ACConfig.ValuesHolder.shouldPoisonEntity()) {
						((LivingEntity) target).addEffect(new EffectInstance(Effects.POISON, 200));
					}
				} else {
					target.playSound(SoundEvents.GENERIC_EAT, 0.5f, 0.5f + target.level.random.nextFloat() / 2);
				}
				if (!player.isCreative()) stack.shrink(1);
				event.setCancellationResult(ActionResultType.sidedSuccess(event.getWorld().isClientSide()));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onUpdateEntity(LivingEvent.LivingUpdateEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof IAgeableEntity && ACConfig.ValuesHolder.isPoisonPotatoCompatEnabled() && ModList.get().isLoaded("quark")) {
			if (entity.getPersistentData().getBoolean(POISON_TAG)) ((IAgeableEntity) entity).resetGrowthProgress();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onNoteBlockPlay(NoteBlockEvent.Play event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getWorld().getBlockState(pos.relative(Direction.DOWN));
		for (DataUtil.CustomNoteBlockInstrument instrument : SORTED_CUSTOM_NOTE_BLOCK_INSTRUMENTS) {
			if (instrument.test(state)) {
				SoundEvent sound = instrument.sound();
				int note = event.getVanillaNoteId();
				float f = (float) Math.pow(2.0D, (double) (note - 12) / 12.0D);
				event.getWorld().playSound(null, pos, sound, SoundCategory.RECORDS, 3.0F, f);
				if (!event.getWorld().isClientSide()) {
					ResourceLocation noteKey = ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.NOTE);
					if (noteKey != null)
						NetworkUtil.spawnParticle(noteKey.toString(), pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, (double) note / 24.0D, 0.0D, 0.0D);
				}
				event.setCanceled(true);
				break;
			}
		}

	}
}
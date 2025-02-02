package com.minecraftabnormals.abnormals_core.core.api;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.ToDoubleFunction;

/**
 * @author SmellyModder(Luke Tonon)
 */
public final class AdvancedRandomPositionGenerator {
	/**
	 * Finds a random target within xz and y
	 */
	@Nullable
	public static Vector3d findRandomTarget(CreatureEntity creature, int xz, int y, boolean goDeep) {
		return findRandomTargetBlock(creature, xz, y, null, goDeep);
	}
	
	@Nullable
	private static Vector3d findRandomTargetBlock(CreatureEntity creature, int xz, int y, @Nullable Vector3d targetVec, boolean goDeep) {
		return generateRandomPos(creature, xz, y, targetVec, true, Math.PI / 2F, goDeep, creature::getWalkTargetValue);
	}
	
	@Nullable
	private static Vector3d generateRandomPos(CreatureEntity creature, int xz, int y, @Nullable Vector3d p_191379_3_, boolean p_191379_4_, double p_191379_5_, boolean goDeep, ToDoubleFunction<BlockPos> p_191379_7_) {
		PathNavigator pathnavigator = creature.getNavigation();
		Random random = creature.getRandom();
		boolean flag = creature.hasRestriction() ? creature.getRestrictCenter().closerThan(creature.position(), (double)(creature.getRestrictRadius() + (float)xz) + 1.0D) : false;
		boolean flag1 = false;
		double d0 = Double.NEGATIVE_INFINITY;
		BlockPos blockpos = new BlockPos(creature.position());

		for (int i = 0; i < 10; ++i) {
			BlockPos blockpos1 = getBlockPos(random, xz, y, p_191379_3_, p_191379_5_, goDeep);
			if (blockpos1 != null) {
				int j = blockpos1.getX();
				int k = blockpos1.getY();
				int l = blockpos1.getZ();
				if (creature.hasRestriction() && xz > 1) {
					BlockPos blockpos2 = creature.getRestrictCenter();
					if (creature.getX() > (double)blockpos2.getX()) {
						j -= random.nextInt(xz / 2);
					} else {
						j += random.nextInt(xz / 2);
					}

					if (creature.getZ() > (double)blockpos2.getZ()) {
						l -= random.nextInt(xz / 2);
					} else {
						l += random.nextInt(xz / 2);
					}
				}

				BlockPos blockpos3 = new BlockPos((double)j + creature.getX(), (double)k + creature.getY(), (double)l + creature.getZ());
				if ((!flag || creature.isWithinRestriction(blockpos3)) && pathnavigator.isStableDestination(blockpos3)) {
					if (!p_191379_4_) {
						blockpos3 = moveAboveSolid(blockpos3, creature);
						if (isWaterDestination(blockpos3, creature)) {
							continue;
						}
					}

					double d1 = p_191379_7_.applyAsDouble(blockpos3);
					if (d1 > d0) {
						d0 = d1;
						blockpos = blockpos3;
						flag1 = true;
					}
				}
			}
		}

		if(flag1) {
			return Vector3d.atCenterOf(blockpos);
		} else {
			return null;
		}
	}
	
	@Nullable
	private static BlockPos getBlockPos(Random rand, int xz, int y, @Nullable Vector3d Vector3d, double angle, boolean goDeep) {
		if(Vector3d != null && !(angle >= Math.PI)) {
			double d3 = MathHelper.atan2(Vector3d.z, Vector3d.x) - (double)((float)Math.PI / 2F);
			double d4 = d3 + (double)(2.0F * rand.nextFloat() - 1.0F) * angle;
			double d0 = Math.sqrt(rand.nextDouble()) * (double)MathHelper.SQRT_OF_TWO * (double)xz;
			double d1 = -d0 * Math.sin(d4);
			double d2 = d0 * Math.cos(d4);
			if(!(Math.abs(d1) > (double)xz) && !(Math.abs(d2) > (double)xz)) {
				double newY = rand.nextInt(2 * y + 1) - y;
				return new BlockPos(d1, newY, d2);
			} else {
				return null;
			}
		} else {
			int newX = rand.nextInt(2 * xz + 1) - xz;
			int newY = rand.nextInt(2 * y + 1) - y;
			int newZ = rand.nextInt(2 * xz + 1) - xz;
			if(goDeep) {
				newY = rand.nextInt(y + 1) - y * 2;
			}
			return new BlockPos(newX, newY, newZ);
		}
	}
	
	private static BlockPos moveAboveSolid(BlockPos pos, CreatureEntity creature) {
		if(!creature.level.getBlockState(pos).getMaterial().isSolid()) {
			return pos;
		} else {
			BlockPos blockpos;
			for(blockpos = pos.above(); blockpos.getY() < creature.level.getMaxBuildHeight() && creature.level.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.above()) {
				;
			}

			return blockpos;
		}
	}

	private static boolean isWaterDestination(BlockPos pos, CreatureEntity creature) {
		return creature.level.getFluidState(pos).is(FluidTags.WATER);
	}
}
package core.registry;

import com.minecraftabnormals.abnormals_core.common.blocks.AbnormalsBeehiveBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.HedgeBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.chest.AbnormalsChestBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.chest.AbnormalsTrappedChestBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.sign.AbnormalsStandingSignBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.sign.AbnormalsWallSignBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.wood.AbnormalsLogBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.wood.WoodPostBlock;
import com.minecraftabnormals.abnormals_core.core.annotations.Test;
import com.minecraftabnormals.abnormals_core.core.util.registry.BlockSubRegistryHelper;
import com.mojang.datafixers.util.Pair;
import common.blocks.ChunkLoadTestBlock;
import common.blocks.RotatedVoxelShapeTestBlock;
import core.ACTest;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Test
@Mod.EventBusSubscriber(modid = ACTest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TestBlocks {
	private static final BlockSubRegistryHelper HELPER = ACTest.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> BLOCK = HELPER.createBlock("block", () -> new Block(Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> TEST_LOADER = HELPER.createBlock("test_loader", () -> new ChunkLoadTestBlock(Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> TEST_ROTATION = HELPER.createBlock("test_rotation", () -> new RotatedVoxelShapeTestBlock(Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final Pair<RegistryObject<AbnormalsStandingSignBlock>, RegistryObject<AbnormalsWallSignBlock>> SIGNS = HELPER.createSignBlock("test", MaterialColor.COLOR_PINK);
	public static final Pair<RegistryObject<AbnormalsChestBlock>, RegistryObject<AbnormalsTrappedChestBlock>> CHESTS = HELPER.createCompatChestBlocks("indev", "test_two", MaterialColor.COLOR_PURPLE);

	public static final RegistryObject<AbnormalsChestBlock> EXAMPLE_CHEST = HELPER.createChestBlock("test", Block.Properties.copy(Blocks.DIRT), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<AbnormalsTrappedChestBlock> EXAMPLE_TRAPPED_CHEST = HELPER.createTrappedChestBlock("test", Block.Properties.copy(Blocks.CHEST), ItemGroup.TAB_DECORATIONS);

	public static final RegistryObject<Block> LOG_BLOCK = HELPER.createBlock("log_block", () -> new AbnormalsLogBlock(() -> Blocks.STRIPPED_ACACIA_LOG, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> BEEHIVE = HELPER.createBlock("example_beehive", () -> new AbnormalsBeehiveBlock(Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> TEST_STRIPPED_POST = HELPER.createBlock("test_stripped_post", () -> new WoodPostBlock(Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> TEST_POST = HELPER.createBlock("test_post", () -> new WoodPostBlock(TEST_STRIPPED_POST, Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> TEST_HEDGE = HELPER.createBlock("test_hedge", () -> new HedgeBlock(Block.Properties.copy(Blocks.DIRT)), ItemGroup.TAB_DECORATIONS);
}

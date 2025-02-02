package com.minecraftabnormals.abnormals_core.common.items;

import com.google.common.collect.Maps;
import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Map;

public class InjectedBlockItem extends BlockItem {
	private static final Map<Item, TargetedItemGroupFiller> FILLER_MAP = Maps.newHashMap();
	private final Item followItem;

	public InjectedBlockItem(Item followItem, Block block, Properties builder) {
		super(block, builder);
		this.followItem = followItem;
		FILLER_MAP.put(followItem, new TargetedItemGroupFiller(() -> followItem));
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER_MAP.get(this.followItem).fillItem(this.asItem(), group, items);
	}
}

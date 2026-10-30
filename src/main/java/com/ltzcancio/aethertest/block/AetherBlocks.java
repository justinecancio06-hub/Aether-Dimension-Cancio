package com.ltzcancio.aethertest.block;

import java.util.function.Function;

import com.ltzcancio.aethertest.AetherDimension;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AetherBlocks {
	public static final Block AETHER_PORTAL = registerBlock("aether_portal", AetherPortalBlock::new, BlockBehaviour.Properties.of().noCollision().noOcclusion().lightLevel(state -> 10).strength(-1.0F, 3600000.0F).noLootTable());

	private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(AetherDimension.MOD_ID, name));
		Block block = factory.apply(properties.setId(blockKey));
		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	public static void initialize() {
		AetherPortalBlock.initialize();
	}
}
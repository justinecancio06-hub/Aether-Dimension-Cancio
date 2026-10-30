package com.ltzcancio.aethertest.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

import com.ltzcancio.aethertest.item.AetherItems;

public class AetherModelProvider extends FabricModelProvider {
	public AetherModelProvider(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateFlatItem(AetherItems.LOOM, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(AetherItems.PRISMARINE_X_SHARD, ModelTemplates.FLAT_ITEM);
	}

	@Override
	public String getName() {
		return "Aether Item Models";
	}
}

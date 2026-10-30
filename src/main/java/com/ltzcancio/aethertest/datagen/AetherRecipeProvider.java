package com.ltzcancio.aethertest.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import com.ltzcancio.aethertest.item.AetherItems;

public class AetherRecipeProvider extends FabricRecipeProvider {
	public AetherRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		return new RecipeProvider(registries, output) {
			@Override
			public void buildRecipes() {
				shaped(RecipeCategory.MISC, AetherItems.LOOM)
						.pattern("P  ")
						.pattern(" M ")
						.pattern("  G")
						.define('P', AetherItems.PRISMARINE_X_SHARD)
						.define('M', Items.PHANTOM_MEMBRANE)
						.define('G', Items.GLOW_INK_SAC)
						.unlockedBy("has_prismarine_x_shard", has(AetherItems.PRISMARINE_X_SHARD))
						.save(output);
			}
		};
	}

	@Override
	public String getName() {
		return "Aether Recipes";
	}
}

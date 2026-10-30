package com.ltzcancio.aethertest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ltzcancio.aethertest.item.AetherItems;

public class AetherDimension implements ModInitializer {
	public static final String MOD_ID = "aether-dimension";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		AetherItems.initialize();
		com.ltzcancio.aethertest.block.AetherBlocks.initialize();
		com.ltzcancio.aethertest.portal.AetherPortalIgnition.initialize();
		AetherCreativeTab.initialize();
		AetherLootTables.initialize();

		CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("ingredients")))
				.register((output) -> {
					output.accept(AetherItems.LOOM);
					output.accept(AetherItems.PRISMARINE_X_SHARD);
				});
	}

	public static net.minecraft.resources.Identifier id(String path) {
		return net.minecraft.resources.Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

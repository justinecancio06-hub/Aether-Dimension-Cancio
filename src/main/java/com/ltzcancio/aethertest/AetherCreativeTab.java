package com.ltzcancio.aethertest;

import com.ltzcancio.aethertest.item.AetherItems;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AetherCreativeTab {
	public static void initialize() {
		CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
				.icon(() -> new ItemStack(AetherItems.LOOM))
				.title(Component.translatable("itemGroup.aether-dimension.aether_dimension"))
				.displayItems((context, entries) -> {
					entries.accept(AetherItems.LOOM);
					entries.accept(AetherItems.PRISMARINE_X_SHARD);
				})
				.build();
		ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(AetherDimension.MOD_ID, "aether_dimension"));
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
	}
}
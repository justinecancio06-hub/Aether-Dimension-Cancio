package com.ltzcancio.aethertest.item;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import com.ltzcancio.aethertest.AetherDimension;

public class AetherItems {
	public static final Item PRISMARINE_X_SHARD = register("prismarine_x_shard", Item::new, new Item.Properties());
	public static final Item LOOM = register("loom", Item::new, new Item.Properties());

	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AetherDimension.MOD_ID, name));

		T item = itemFactory.apply(settings.setId(itemKey));

		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

	public static void initialize() {
	}
}

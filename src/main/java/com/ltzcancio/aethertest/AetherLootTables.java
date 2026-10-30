package com.ltzcancio.aethertest;

import java.util.Collections;

import com.ltzcancio.aethertest.item.AetherItems;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class AetherLootTables {
	private static final ResourceKey<LootTable> ELDER_GUARDIAN_KEY = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("minecraft", "entities/elder_guardian"));

	public static void initialize() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			AetherDimension.LOGGER.info("Modifying loot table: " + key);
			if (key.equals(ELDER_GUARDIAN_KEY) && source.isBuiltin()) {
				LootPool.Builder poolBuilder = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0f))
						.add(LootItem.lootTableItem(AetherItems.PRISMARINE_X_SHARD));
				tableBuilder.pools(Collections.singletonList(poolBuilder.build()));
			}
		});
	}
}
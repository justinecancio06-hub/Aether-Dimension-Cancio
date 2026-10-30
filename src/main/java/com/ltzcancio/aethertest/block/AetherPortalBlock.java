package com.ltzcancio.aethertest.block;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import com.ltzcancio.aethertest.AetherDimension;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AetherPortalBlock extends Block {
	public static final ResourceKey<Level> AETHER_DIMENSION = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(AetherDimension.MOD_ID, "aether"));
	public static final ResourceKey<Level> OVERWORLD = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("overworld"));
	private static final BlockPos[] NEIGHBORS = {new BlockPos(0,0,-1), new BlockPos(0,0,1), new BlockPos(1,0,0), new BlockPos(-1,0,0), new BlockPos(0,1,0), new BlockPos(0,-1,0)};

	public AetherPortalBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean flag) {
		teleportIfInPortal(level, entity, pos);
	}

	private void teleportIfInPortal(Level level, Entity entity, BlockPos pos) {
		if (level.isClientSide()) {
			return;
		}
		if (entity instanceof ServerPlayer player) {
			if (player.isPassenger() || player.isVehicle() || !player.canUsePortal(false)) {
				return;
			}
			if (player.getPortalCooldown() <= 0) {
				player.setPortalCooldown(300);
				teleportPlayer(player, pos);
			}
			return;
		}
		if (level instanceof ServerLevel serverLevel && !entity.isPassenger() && !entity.isVehicle()) {
			if (entity.getPortalCooldown() <= 0) {
				entity.setPortalCooldown(300);
				teleportEntity(serverLevel, entity, pos);
			}
		}
	}

	private void teleportPlayer(ServerPlayer player, BlockPos pos) {
		MinecraftServer server = player.level().getServer();
		if (server == null) {
			return;
		}
		ResourceKey<Level> current = player.level().dimension();
		if (current.equals(OVERWORLD)) {
			ServerLevel aetherLevel = server.getLevel(AETHER_DIMENSION);
			if (aetherLevel != null) {
				BlockPos target = new BlockPos(pos.getX(), 100, pos.getZ());
				aetherLevel.getChunkAt(target);
				createSafetyPlatform(aetherLevel, target);
				player.teleportTo(aetherLevel, target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5, java.util.Set.of(), player.getYRot(), player.getXRot(), false);
			}
		} else if (current.equals(AETHER_DIMENSION)) {
			ServerLevel overworldLevel = server.getLevel(OVERWORLD);
			if (overworldLevel != null) {
				BlockPos target = new BlockPos(pos.getX(), 100, pos.getZ());
				overworldLevel.getChunkAt(target);
				createSafetyPlatform(overworldLevel, target);
				player.teleportTo(overworldLevel, target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5, java.util.Set.of(), player.getYRot(), player.getXRot(), false);
			}
		}
	}

	private void teleportEntity(ServerLevel level, Entity entity, BlockPos pos) {
		MinecraftServer server = level.getServer();
		if (server == null) {
			return;
		}
		ResourceKey<Level> current = level.dimension();
		if (current.equals(OVERWORLD)) {
			ServerLevel aetherLevel = server.getLevel(AETHER_DIMENSION);
			if (aetherLevel != null) {
				BlockPos target = new BlockPos(pos.getX(), 100, pos.getZ());
				aetherLevel.getChunkAt(target);
				createSafetyPlatform(aetherLevel, target);
				entity.teleportTo(aetherLevel, target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5, java.util.Set.of(), entity.getYRot(), entity.getXRot(), false);
			}
		} else if (current.equals(AETHER_DIMENSION)) {
			ServerLevel overworldLevel = server.getLevel(OVERWORLD);
			if (overworldLevel != null) {
				BlockPos target = new BlockPos(pos.getX(), 100, pos.getZ());
				overworldLevel.getChunkAt(target);
				createSafetyPlatform(overworldLevel, target);
				entity.teleportTo(overworldLevel, target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5, java.util.Set.of(), entity.getYRot(), entity.getXRot(), false);
			}
		}
	}

	private void createSafetyPlatform(Level level, BlockPos center) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockPos p = center.offset(x, -1, z);
				if (level.getBlockState(p).isAir() || level.getBlockState(p).getBlock() instanceof AetherPortalBlock) {
					level.setBlock(p, Blocks.GLOWSTONE.defaultBlockState(), 3);
				}
			}
		}
		level.setBlock(center, AetherBlocks.AETHER_PORTAL.defaultBlockState(), 3);
	}

	public static void initialize() {
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (level.isClientSide()) {
				return;
			}
			if (state.is(Blocks.GLOWSTONE)) {
				breakPortalAround(level, pos);
				return;
			}
			if (state.getBlock() instanceof AetherPortalBlock) {
				breakPortalAround(level, pos);
				return;
			}
		});
	}

	private static void breakPortalAround(Level level, BlockPos start) {
		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new ArrayDeque<>();
		queue.add(start);
		visited.add(start);
		while (!queue.isEmpty()) {
			BlockPos current = queue.poll();
			if (level.getBlockState(current).getBlock() instanceof AetherPortalBlock) {
				level.setBlock(current, Blocks.AIR.defaultBlockState(), 3);
			}
			for (BlockPos n : NEIGHBORS) {
				BlockPos next = current.offset(n);
				if (!visited.contains(next) && level.getBlockState(next).getBlock() instanceof AetherPortalBlock) {
					visited.add(next);
					queue.add(next);
				}
			}
		}
	}
}
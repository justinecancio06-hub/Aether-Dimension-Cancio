package com.ltzcancio.aethertest.portal;

import com.ltzcancio.aethertest.AetherDimension;
import com.ltzcancio.aethertest.block.AetherBlocks;
import com.ltzcancio.aethertest.item.AetherItems;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AetherPortalIgnition {
	private static final int MIN_WIDTH = 2;
	private static final int MAX_WIDTH = 21;
	private static final int MIN_HEIGHT = 3;
	private static final int MAX_HEIGHT = 21;

	public static void initialize() {
		AetherDimension.LOGGER.info("Registering portal ignition callback");
		UseBlockCallback.EVENT.register((Player player, Level level, InteractionHand hand, BlockHitResult hitResult) -> {
			AetherDimension.LOGGER.info("Ignition callback fired");
			if (level.isClientSide()) {
				return InteractionResult.PASS;
			}
			ItemStack stack = player.getMainHandItem();
			AetherDimension.LOGGER.info("Held item is loom: " + stack.is(AetherItems.LOOM));
			if (!stack.is(AetherItems.LOOM)) {
				return InteractionResult.PASS;
			}
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = level.getBlockState(pos);
			AetherDimension.LOGGER.info("Clicked block: " + state.getBlock());
			if (state.getBlock() != Blocks.GLOWSTONE) {
				AetherDimension.LOGGER.info("Frame validation result: false");
				return InteractionResult.PASS;
			}

			PortalResult result = findAndValidatePortal(level, pos);
			AetherDimension.LOGGER.info("Found width: " + result.width + ", height: " + result.height + ", border valid: " + result.borderValid + ", interior empty: " + result.interiorEmpty);
			if (!result.valid) {
				AetherDimension.LOGGER.info("Frame validation result: false");
				return InteractionResult.PASS;
			}
			AetherDimension.LOGGER.info("Frame validation result: true");

			if (placePortalInterior(level, result.bottomLeft, result.topRight, result.axisX)) {
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
	}

	private static class PortalResult {
		boolean valid;
		boolean borderValid;
		boolean interiorEmpty;
		boolean axisX;
		int width;
		int height;
		BlockPos bottomLeft;
		BlockPos topRight;
	}

	private static PortalResult findAndValidatePortal(Level level, BlockPos clickedGlowstone) {
		PortalResult result = new PortalResult();
		result.borderValid = false;
		result.interiorEmpty = false;
		result.valid = false;

		boolean axisX = isXAxisPortal(level, clickedGlowstone);
		result.axisX = axisX;

		if (axisX) {
			BlockPos bottomLeft = findBottomLeftAlongX(level, clickedGlowstone);
			BlockPos topLeft = findTopLeftAlongX(level, bottomLeft);
			BlockPos topRight = findTopRightAlongX(level, topLeft);
			int width = topRight.getX() - bottomLeft.getX() - 1;
			int height = topRight.getY() - bottomLeft.getY() - 1;
			if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) {
				result.width = width;
				result.height = height;
				result.borderValid = false;
				result.interiorEmpty = false;
				return result;
			}
			boolean borderValid = isBorderValidX(level, bottomLeft, topRight);
			boolean interiorEmpty = isInteriorEmptyX(level, bottomLeft, topRight);
			result.width = width;
			result.height = height;
			result.borderValid = borderValid;
			result.interiorEmpty = interiorEmpty;
			result.bottomLeft = bottomLeft;
			result.topRight = topRight;
			result.valid = borderValid && interiorEmpty;
			return result;
		} else {
			BlockPos bottomLeft = findBottomLeftAlongZ(level, clickedGlowstone);
			BlockPos topLeft = findTopLeftAlongZ(level, bottomLeft);
			BlockPos topRight = findTopRightAlongZ(level, topLeft);
			int width = topRight.getZ() - bottomLeft.getZ() - 1;
			int height = topRight.getY() - bottomLeft.getY() - 1;
			if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) {
				result.width = width;
				result.height = height;
				result.borderValid = false;
				result.interiorEmpty = false;
				return result;
			}
			boolean borderValid = isBorderValidZ(level, bottomLeft, topRight);
			boolean interiorEmpty = isInteriorEmptyZ(level, bottomLeft, topRight);
			result.width = width;
			result.height = height;
			result.borderValid = borderValid;
			result.interiorEmpty = interiorEmpty;
			result.bottomLeft = bottomLeft;
			result.topRight = topRight;
			result.valid = borderValid && interiorEmpty;
			return result;
		}
	}

	private static boolean isXAxisPortal(Level level, BlockPos pos) {
		BlockPos north = pos.north();
		BlockPos south = pos.south();
		return level.getBlockState(north).isAir() || level.getBlockState(south).isAir();
	}

	private static BlockPos findBottomLeftAlongX(Level level, BlockPos clicked) {
		BlockPos current = clicked;
		while (level.getBlockState(current.below()).getBlock() == Blocks.GLOWSTONE) {
			current = current.below();
		}
		while (level.getBlockState(current.west()).getBlock() == Blocks.GLOWSTONE) {
			current = current.west();
		}
		return current;
	}

	private static BlockPos findTopLeftAlongX(Level level, BlockPos bottomLeft) {
		BlockPos current = bottomLeft;
		while (level.getBlockState(current.above()).getBlock() == Blocks.GLOWSTONE) {
			current = current.above();
		}
		return current;
	}

	private static BlockPos findTopRightAlongX(Level level, BlockPos topLeft) {
		BlockPos current = topLeft;
		while (level.getBlockState(current.east()).getBlock() == Blocks.GLOWSTONE) {
			current = current.east();
		}
		return current;
	}

	private static boolean isBorderValidX(Level level, BlockPos bottomLeft, BlockPos topRight) {
		int minX = bottomLeft.getX();
		int maxX = topRight.getX();
		int minY = bottomLeft.getY();
		int maxY = topRight.getY();
		int z = bottomLeft.getZ();
		for (int x = minX; x <= maxX; x++) {
			if (level.getBlockState(new BlockPos(x, minY, z)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
			if (level.getBlockState(new BlockPos(x, maxY, z)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
		}
		for (int y = minY; y <= maxY; y++) {
			if (level.getBlockState(new BlockPos(minX, y, z)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
			if (level.getBlockState(new BlockPos(maxX, y, z)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
		}
		return true;
	}

	private static boolean isInteriorEmptyX(Level level, BlockPos bottomLeft, BlockPos topRight) {
		int minX = bottomLeft.getX();
		int maxX = topRight.getX();
		int minY = bottomLeft.getY();
		int maxY = topRight.getY();
		int z = bottomLeft.getZ();
		for (int x = minX + 1; x < maxX; x++) {
			for (int y = minY + 1; y < maxY; y++) {
				if (!level.getBlockState(new BlockPos(x, y, z)).isAir()) {
					return false;
				}
			}
		}
		return true;
	}

	private static BlockPos findBottomLeftAlongZ(Level level, BlockPos clicked) {
		BlockPos current = clicked;
		while (level.getBlockState(current.below()).getBlock() == Blocks.GLOWSTONE) {
			current = current.below();
		}
		while (level.getBlockState(current.north()).getBlock() == Blocks.GLOWSTONE) {
			current = current.north();
		}
		return current;
	}

	private static BlockPos findTopLeftAlongZ(Level level, BlockPos bottomLeft) {
		BlockPos current = bottomLeft;
		while (level.getBlockState(current.above()).getBlock() == Blocks.GLOWSTONE) {
			current = current.above();
		}
		return current;
	}

	private static BlockPos findTopRightAlongZ(Level level, BlockPos topLeft) {
		BlockPos current = topLeft;
		while (level.getBlockState(current.south()).getBlock() == Blocks.GLOWSTONE) {
			current = current.south();
		}
		return current;
	}

	private static boolean isBorderValidZ(Level level, BlockPos bottomLeft, BlockPos topRight) {
		int minX = bottomLeft.getX();
		int maxZ = topRight.getZ();
		int minY = bottomLeft.getY();
		int maxY = topRight.getY();
		for (int z = bottomLeft.getZ(); z <= maxZ; z++) {
			if (level.getBlockState(new BlockPos(minX, minY, z)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
			if (level.getBlockState(new BlockPos(minX, maxY, z)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
		}
		for (int y = minY; y <= maxY; y++) {
			if (level.getBlockState(new BlockPos(minX, y, bottomLeft.getZ())).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
			if (level.getBlockState(new BlockPos(minX, y, maxZ)).getBlock() != Blocks.GLOWSTONE) {
				return false;
			}
		}
		return true;
	}

	private static boolean isInteriorEmptyZ(Level level, BlockPos bottomLeft, BlockPos topRight) {
		int minX = bottomLeft.getX();
		int maxZ = topRight.getZ();
		int minY = bottomLeft.getY();
		int maxY = topRight.getY();
		for (int z = bottomLeft.getZ() + 1; z < maxZ; z++) {
			for (int y = minY + 1; y < maxY; y++) {
				if (!level.getBlockState(new BlockPos(minX, y, z)).isAir()) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean placePortalInterior(Level level, BlockPos bottomLeft, BlockPos topRight, boolean axisX) {
		if (axisX) {
			for (int x = bottomLeft.getX() + 1; x < topRight.getX(); x++) {
				for (int y = bottomLeft.getY() + 1; y < topRight.getY(); y++) {
					BlockPos p = new BlockPos(x, y, bottomLeft.getZ());
					level.setBlock(p, AetherBlocks.AETHER_PORTAL.defaultBlockState(), 3);
					AetherDimension.LOGGER.info("Placing portal blocks at " + p.getX() + "," + p.getY() + "," + p.getZ());
				}
			}
		} else {
			for (int z = bottomLeft.getZ() + 1; z < maxZ(topRight); z++) {
				for (int y = bottomLeft.getY() + 1; y < topRight.getY(); y++) {
					BlockPos p = new BlockPos(bottomLeft.getX(), y, z);
					level.setBlock(p, AetherBlocks.AETHER_PORTAL.defaultBlockState(), 3);
					AetherDimension.LOGGER.info("Placing portal blocks at " + p.getX() + "," + p.getY() + "," + p.getZ());
				}
			}
		}
		return true;
	}

	private static int maxZ(BlockPos topRight) {
		return topRight.getZ();
	}
}
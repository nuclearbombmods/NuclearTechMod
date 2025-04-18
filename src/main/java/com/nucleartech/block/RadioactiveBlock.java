package com.nucleartech.block;

import com.mojang.logging.LogUtils;
import com.nucleartech.radiation.IRadioactive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class RadioactiveBlock extends Block implements IRadioactive {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final float baseRadiationLevel;
    private final float radiationRange;
    private final int tickInterval;

    public RadioactiveBlock(Properties properties, float baseRadiationLevel, float radiationRange, int tickInterval) {
        super(properties);
        this.baseRadiationLevel = baseRadiationLevel;
        this.radiationRange = radiationRange;
        this.tickInterval = tickInterval;
    }

    @Override
    public float getBaseRadiationLevel() {
        return baseRadiationLevel;
    }

    @Override
    public float getRadiationRange() {
        return radiationRange;
    }

    @Override
    public int getTickInterval() {
        return tickInterval;
    }

    private void checkNearbyPlayers(Level level, BlockPos pos) {
        if (!level.isClientSide && level.getGameTime() % tickInterval == 0) {
            level.getEntitiesOfClass(Player.class, 
                level.getBlockState(pos).getCollisionShape(level, pos).bounds()
                    .move(pos)
                    .inflate(radiationRange))
                .forEach(player -> {
                    double distance = Math.sqrt(player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                    if (distance <= radiationRange) {
                        LOGGER.info("Player {} is within radiation range of {} at {} blocks ({} mSv/h)", 
                            player.getName().getString(),
                            this.getDescriptionId(),
                            String.format("%.2f", distance),
                            String.format("%.2f", baseRadiationLevel / (distance * distance)));
                    }
                });
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            LOGGER.info("Radioactive block placed at {}: {} ({} mSv/h, {} blocks range)", 
                pos, this.getDescriptionId(), baseRadiationLevel, radiationRange);
            checkNearbyPlayers(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (!level.isClientSide) {
            LOGGER.info("Radioactive block removed at {}: {}", pos, this.getDescriptionId());
        }
    }
} 
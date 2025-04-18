package com.nucleartech.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.nucleartech.registry.ModBlocks;
import com.nucleartech.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class RadiationManager {
    private static final float BASE_RADIATION_RANGE = 5.0f;
    private static final float LEAD_SHIELDING_FACTOR = 0.5f; // Each lead block reduces radiation by 50%

    public static float calculateRadiationLevel(Level level, BlockPos pos, float baseRadiation) {
        float radiation = baseRadiation;
        
        // Check for shielding blocks in a 3x3x3 area around the position
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    
                    if (state.is(ModBlocks.LEAD_BLOCK.get())) {
                        radiation *= LEAD_SHIELDING_FACTOR;
                    }
                }
            }
        }
        
        return radiation;
    }

    public static void applyRadiationEffect(LivingEntity entity, float radiationLevel) {
        if (radiationLevel > 0) {
            // Convert radiation level to effect amplifier (0-4)
            int amplifier = Math.min(4, (int) (radiationLevel / 2));
            entity.addEffect(new MobEffectInstance(
                ModEffects.RADIATION.get(),
                200, // 10 seconds
                amplifier,
                false,
                true
            ));
        }
    }

    public static float getDistanceFactor(float distance) {
        // Radiation follows inverse square law
        return 1.0f / (distance * distance);
    }
} 
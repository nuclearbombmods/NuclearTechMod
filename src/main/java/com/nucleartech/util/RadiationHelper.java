package com.nucleartech.util;

import com.nucleartech.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class RadiationHelper {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static void applyRadiationEffect(LivingEntity entity, double radiationLevel) {
        if (radiationLevel <= 0) return;
        
        int amplifier = calculateAmplifier(radiationLevel);
        int duration = 200; // 10 seconds base duration
        
        // Apply the effect with calculated amplifier
        entity.addEffect(new MobEffectInstance(ModEffects.RADIATION.get(), duration, amplifier, false, true));
        
        // Log radiation exposure
        LOGGER.info("Entity {} exposed to {} mSv/h radiation (Amplifier: {})", 
            entity.getName().getString(), 
            String.format("%.2f", radiationLevel), 
            amplifier);
            
        // Visual and sound effects based on radiation level
        Level level = entity.level();
        if (level.isClientSide) {
            if (radiationLevel >= 50) { // Critical radiation
                level.playSound(null, entity.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.5F, 1.0F);
            } else if (radiationLevel >= 20) { // Severe radiation
                level.playSound(null, entity.blockPosition(), SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 0.3F, 1.0F);
            }
        }
    }
    
    private static int calculateAmplifier(double radiationLevel) {
        if (radiationLevel >= 50) return 4; // Critical
        if (radiationLevel >= 20) return 3; // Severe
        if (radiationLevel >= 5) return 2;  // Moderate
        if (radiationLevel >= 1) return 1;  // Mild
        return 0;
    }
    
    public static double calculateRadiationLevel(double baseLevel, double distance) {
        if (distance <= 0) return baseLevel;
        // Inverse square law for radiation intensity
        return baseLevel / (distance * distance);
    }
    
    public static void spawnRadiationParticles(Level level, BlockPos pos, double radiationLevel) {
        if (level.isClientSide && radiationLevel > 0) {
            int particleCount = (int) (radiationLevel / 5);
            particleCount = Math.min(particleCount, 20); // Limit maximum particles
            
            for (int i = 0; i < particleCount; i++) {
                double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2;
                double y = pos.getY() + 0.5 + level.random.nextDouble() * 2;
                double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2;
                
                level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0, 0);
            }
        }
    }
} 
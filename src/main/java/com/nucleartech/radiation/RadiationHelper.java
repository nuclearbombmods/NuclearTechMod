package com.nucleartech.radiation;

import com.mojang.logging.LogUtils;
import com.nucleartech.registry.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import org.slf4j.Logger;

public class RadiationHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void applyRadiationEffect(LivingEntity entity, double radiationLevel, int tickInterval) {
        if (radiationLevel > 0) {
            int amplifier = calculateAmplifier(radiationLevel);
            LOGGER.info("Applying radiation effect to {}: {} mSv/h -> amplifier {}", 
                entity.getName().getString(), radiationLevel, amplifier);
            entity.addEffect(new MobEffectInstance(
                ModEffects.RADIATION.get(),
                tickInterval,
                amplifier,
                false,
                true
            ));
        } else {
            LOGGER.info("No radiation effect applied to {} (radiation level: {} mSv/h)", 
                entity.getName().getString(), radiationLevel);
        }
    }

    public static double calculateRadiationLevel(double distance, float baseRadiation) {
        // Modified inverse square law with a minimum distance factor
        double minDistance = 0.1; // Reduced minimum distance for more noticeable radiation
        double effectiveDistance = Math.max(distance, minDistance);
        double baseRadiationLevel = baseRadiation / (effectiveDistance * effectiveDistance);
        
        // Add a significant constant radiation level for very close proximity
        double closeProximityBonus = 0.0;
        if (distance < 1.0) {
            closeProximityBonus = baseRadiation * 0.5; // Increased from 0.1 to 0.5
        }
        
        double finalRadiationLevel = baseRadiationLevel + closeProximityBonus;
        
        LOGGER.info("Radiation calculation for distance {} blocks: base={} mSv/h, closeProximityBonus={} mSv/h, final={} mSv/h", 
            distance, baseRadiationLevel, closeProximityBonus, finalRadiationLevel);
            
        return Math.max(0, finalRadiationLevel);
    }

    public static int calculateAmplifier(double radiationLevel) {
        // Scale the amplifier calculation to be more sensitive to lower radiation levels
        int amplifier = (int) Math.min(4, Math.max(0, radiationLevel / 0.5)); // Reduced from 1.0 to 0.5
        LOGGER.info("Calculating amplifier for radiation level {} mSv/h -> amplifier {}", 
            radiationLevel, amplifier);
        return amplifier;
    }

    public static void checkNearbyRadioactiveItems(Level level, LivingEntity entity, IRadioactive radioactive) {
        if (!level.isClientSide && level.getGameTime() % radioactive.getTickInterval() == 0) {
            // Get all items in the world within radiation range
            level.getEntitiesOfClass(ItemEntity.class, 
                entity.getBoundingBox().inflate(radioactive.getRadiationRange()))
                .stream()
                .filter(itemEntity -> itemEntity.getItem().getItem() instanceof IRadioactive)
                .forEach(itemEntity -> {
                    double distance = entity.distanceTo(itemEntity);
                    double radiationLevel = calculateRadiationLevel(distance, radioactive.getBaseRadiationLevel());
                    applyRadiationEffect(entity, radiationLevel, radioactive.getTickInterval());
                });
        }
    }
} 
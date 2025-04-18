package com.nucleartech.item;

import com.mojang.logging.LogUtils;
import com.nucleartech.radiation.IRadioactive;
import com.nucleartech.radiation.RadiationHelper;
import com.nucleartech.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import com.nucleartech.NuclearTechMod;

public class UraniumFuelRodItem extends ModItem implements IRadioactive {
    private static final float BASE_RADIATION_LEVEL = 50.0f; // Base radiation level for a fuel rod
    private static final float RADIATION_RANGE = 10.0f; // Radiation range for a fuel rod
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int TICK_INTERVAL = 20; // Check every second (20 ticks)

    public UraniumFuelRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getBaseRadiationLevel() {
        return BASE_RADIATION_LEVEL;
    }

    @Override
    public float getRadiationRange() {
        return RADIATION_RANGE;
    }

    @Override
    public int getTickInterval() {
        return TICK_INTERVAL;
    }

    @Override
    protected void addTooltipDetails(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Highly Radioactive!").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Radiation Level: " + BASE_RADIATION_LEVEL + " mSv/h").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("Range: " + RADIATION_RANGE + " blocks").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("Warning:").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("- Causes radiation damage to nearby entities").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("- Use lead blocks for shielding").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            // Only process every TICK_INTERVAL ticks
            if (level.getGameTime() % TICK_INTERVAL != 0) {
                return;
            }

            // Debug: Log when inventory tick occurs
            LOGGER.info("Uranium fuel rod inventory tick - Entity: {}, Slot: {}, Selected: {}", 
                entity.getName().getString(), slotId, isSelected);

            // Always apply radiation to the holder
            double holderRadiation = RadiationHelper.calculateRadiationLevel(0.1, BASE_RADIATION_LEVEL);
            LOGGER.info("Applying radiation to holder: {} mSv/h", holderRadiation);
            RadiationHelper.applyRadiationEffect(livingEntity, holderRadiation, TICK_INTERVAL);

            // Check for nearby entities
            List<Entity> nearbyEntities = level.getEntities(entity, entity.getBoundingBox().inflate(RADIATION_RANGE));
            LOGGER.info("Found {} nearby entities within {} blocks", nearbyEntities.size(), RADIATION_RANGE);
            
            for (Entity nearbyEntity : nearbyEntities) {
                if (nearbyEntity instanceof LivingEntity nearbyLiving && nearbyEntity != entity) {
                    double distance = entity.distanceTo(nearbyEntity);
                    double radiationLevel = RadiationHelper.calculateRadiationLevel(distance, BASE_RADIATION_LEVEL);
                    LOGGER.info("Applying radiation to nearby entity: {} at {} blocks ({} mSv/h)", 
                        nearbyEntity.getName().getString(), distance, radiationLevel);
                    RadiationHelper.applyRadiationEffect(nearbyLiving, radiationLevel, TICK_INTERVAL);
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Make it glow to indicate radioactivity
    }
} 
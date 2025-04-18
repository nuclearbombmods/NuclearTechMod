package com.nucleartech.item;

import com.nucleartech.radiation.IRadioactive;
import com.nucleartech.radiation.RadiationHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadiationDetectorItem extends ModItem {
    private static final float DETECTION_RANGE = 10.0f; // Range in blocks
    private static final int COOLDOWN_TICKS = 20; // 1 second cooldown
    private static final float MIN_DETECTABLE_RADIATION = 0.01f; // Reduced from 0.1f to 0.01f for better sensitivity
    private static final Logger LOGGER = LoggerFactory.getLogger(RadiationDetectorItem.class);

    public RadiationDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void addTooltipDetails(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Measures Radiation Levels").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("Usage:").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("- Right-click to measure radiation").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("- Shows radiation in mSv/h").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("- Safe level: < 1.0 mSv/h").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("- Caution level: 1.0-5.0 mSv/h").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("- Dangerous level: > 5.0 mSv/h").withStyle(ChatFormatting.RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide()) {
            // Check cooldown
            if (player.getCooldowns().isOnCooldown(this)) {
                LOGGER.info("Radiation detector on cooldown for player {}", player.getName().getString());
                return InteractionResultHolder.fail(stack);
            }

            // Calculate total radiation from nearby radioactive items
            double totalRadiation = 0.0;
            int itemCount = 0;
            
            for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, 
                    player.getBoundingBox().inflate(DETECTION_RANGE))) {
                ItemStack itemStack = itemEntity.getItem();
                if (itemStack.getItem() instanceof IRadioactive radioactive) {
                    double distance = player.distanceTo(itemEntity);
                    double radiation = RadiationHelper.calculateRadiationLevel(distance, radioactive.getBaseRadiationLevel());
                    totalRadiation += radiation;
                    itemCount++;
                    LOGGER.info("Detected radioactive item: {} at {} blocks ({} mSv/h)", 
                        itemStack.getItem().getDescriptionId(), distance, radiation);
                }
            }

            // Calculate radiation from nearby radioactive blocks
            int blockCount = 0;
            BlockPos playerPos = player.blockPosition();
            int detectionRange = (int)DETECTION_RANGE;
            for (int x = -detectionRange; x <= detectionRange; x++) {
                for (int y = -detectionRange; y <= detectionRange; y++) {
                    for (int z = -detectionRange; z <= detectionRange; z++) {
                        BlockPos pos = playerPos.offset(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        if (state.getBlock() instanceof IRadioactive radioactive) {
                            double distance = Math.sqrt(x * x + y * y + z * z);
                            double radiation = RadiationHelper.calculateRadiationLevel(distance, radioactive.getBaseRadiationLevel());
                            totalRadiation += radiation;
                            blockCount++;
                            LOGGER.info("Detected radioactive block: {} at {} blocks ({} mSv/h)", 
                                state.getBlock().getDescriptionId(), distance, radiation);
                        }
                    }
                }
            }

            LOGGER.info("Total radiation measurement: {} mSv/h ({} items, {} blocks)", 
                totalRadiation, itemCount, blockCount);

            // Format the message based on radiation level
            String message;
            if (totalRadiation < MIN_DETECTABLE_RADIATION) {
                message = "§aNo detectable radiation";
            } else if (totalRadiation < 1.0) {
                message = String.format("§aSafe: %.2f mSv/h", totalRadiation);
            } else if (totalRadiation < 5.0) {
                message = String.format("§eCaution: %.2f mSv/h", totalRadiation);
            } else {
                message = String.format("§cDANGEROUS: %.2f mSv/h", totalRadiation);
            }

            player.sendSystemMessage(Component.literal(message));
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
} 
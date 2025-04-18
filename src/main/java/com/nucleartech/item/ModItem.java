package com.nucleartech.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.List;

public class ModItem extends Item {
    public ModItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        addTooltipDetails(stack, level, tooltip, flag);
        super.appendHoverText(stack, level, tooltip, flag);
    }

    protected void addTooltipDetails(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        // Override in subclasses to add specific tooltips
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack);
    }
} 
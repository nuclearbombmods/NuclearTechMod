package com.nucleartech.registry;

import com.nucleartech.NuclearTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NuclearTechMod.MODID);

    public static final RegistryObject<CreativeModeTab> NUCLEAR_TAB = CREATIVE_MODE_TABS.register("nuclear_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.URANIUM_FUEL_ROD.get()))
                    .title(Component.translatable("creativetab.nuclear_tab"))
                    .displayItems((parameters, output) -> {
                        // Add blocks
                        output.accept(ModBlocks.URANIUM_ORE.get());
                        output.accept(ModBlocks.URANIUM_BLOCK.get());
                        output.accept(ModBlocks.LEAD_BLOCK.get());
                        output.accept(ModBlocks.REACTOR_CASING.get());

                        // Add raw materials
                        output.accept(ModItems.RAW_URANIUM.get());
                        output.accept(ModItems.URANIUM_INGOT.get());
                        output.accept(ModItems.LEAD_INGOT.get());

                        // Add nuclear components
                        output.accept(ModItems.URANIUM_FUEL_ROD.get());
                        output.accept(ModItems.DEPLETED_FUEL_ROD.get());
                        output.accept(ModItems.RADIATION_DETECTOR.get());
                    })
                    .build());
} 
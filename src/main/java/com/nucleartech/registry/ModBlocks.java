package com.nucleartech.registry;

import com.nucleartech.NuclearTechMod;
import com.nucleartech.block.RadioactiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NuclearTechMod.MODID);

    // Uranium Ore
    public static final RegistryObject<Block> URANIUM_ORE = BLOCKS.register("uranium_ore",
            () -> new RadioactiveBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(3.0f, 3.0f)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_GREEN),
                    10.0f, // Base radiation level (mSv/h)
                    5.0f,  // Radiation range (blocks)
                    20));  // Tick interval

    // Uranium Block
    public static final RegistryObject<Block> URANIUM_BLOCK = BLOCKS.register("uranium_block",
            () -> new RadioactiveBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_GREEN),
                    30.0f, // Base radiation level (mSv/h)
                    8.0f,  // Radiation range (blocks)
                    20));  // Tick interval

    // Lead Block (for radiation shielding)
    public static final RegistryObject<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(4.0f, 4.0f)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)));

    // Reactor Casing
    public static final RegistryObject<Block> REACTOR_CASING = BLOCKS.register("reactor_casing",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(6.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.COLOR_GRAY)));
} 
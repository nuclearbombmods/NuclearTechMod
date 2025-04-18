package com.nucleartech.registry;

import com.nucleartech.NuclearTechMod;
import com.nucleartech.item.RadiationDetectorItem;
import com.nucleartech.item.UraniumFuelRodItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NuclearTechMod.MODID);

    // Block Items
    public static final RegistryObject<Item> URANIUM_ORE_ITEM = ITEMS.register("uranium_ore",
            () -> new BlockItem(ModBlocks.URANIUM_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_BLOCK_ITEM = ITEMS.register("uranium_block",
            () -> new BlockItem(ModBlocks.URANIUM_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> LEAD_BLOCK_ITEM = ITEMS.register("lead_block",
            () -> new BlockItem(ModBlocks.LEAD_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> REACTOR_CASING_ITEM = ITEMS.register("reactor_casing",
            () -> new BlockItem(ModBlocks.REACTOR_CASING.get(), new Item.Properties()));

    // Raw Materials
    public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot",
            () -> new Item(new Item.Properties()));

    // Nuclear Components
    public static final RegistryObject<Item> URANIUM_FUEL_ROD = ITEMS.register("uranium_fuel_rod",
            () -> new UraniumFuelRodItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DEPLETED_FUEL_ROD = ITEMS.register("depleted_fuel_rod",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> RADIATION_DETECTOR = ITEMS.register("radiation_detector",
            () -> new RadiationDetectorItem(new Item.Properties().stacksTo(1)));
} 
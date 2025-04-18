package com.nucleartech.effect;

import com.nucleartech.NuclearTechMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NuclearTechMod.MODID);

    public static final RegistryObject<MobEffect> RADIATION = MOB_EFFECTS.register("radiation",
            RadiationEffect::new);
} 
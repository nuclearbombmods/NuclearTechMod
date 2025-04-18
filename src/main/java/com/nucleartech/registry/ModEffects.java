package com.nucleartech.registry;

import com.nucleartech.NuclearTechMod;
import com.nucleartech.radiation.RadiationHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NuclearTechMod.MODID);

    public static final RegistryObject<MobEffect> RADIATION = MOB_EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, 0x00FF00));

    public static class RadiationEffect extends MobEffect {
        public RadiationEffect(MobEffectCategory category, int color) {
            super(category, color);
        }

        @Override
        public void applyEffectTick(LivingEntity entity, int amplifier) {
            Level level = entity.level();
            if (!level.isClientSide) {
                // Apply damage based on amplifier
                if (amplifier > 0) {
                    entity.hurt(level.damageSources().magic(), amplifier * 0.5f);
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return true;
        }
    }
} 
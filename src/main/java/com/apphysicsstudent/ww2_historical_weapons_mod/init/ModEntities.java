package com.apphysicsstudent.ww2_historical_weapons_mod.init;

import com.apphysicsstudent.ww2_historical_weapons_mod.entity.StickGrenadeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "ww2_historical_weapons_mod");

    public static final DeferredHolder<EntityType<?>, EntityType<StickGrenadeEntity>> STICK_GRENADE =
            ENTITY_TYPES.register("stick_grenade", location ->
                    EntityType.Builder.<StickGrenadeEntity>of(StickGrenadeEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, location))
            );

    // This method resolves the error in WW2Historicalweaponsmod.java
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
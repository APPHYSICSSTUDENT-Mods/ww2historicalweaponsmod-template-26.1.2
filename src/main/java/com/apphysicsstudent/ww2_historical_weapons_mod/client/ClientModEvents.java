package com.apphysicsstudent.ww2_historical_weapons_mod.client;

import com.apphysicsstudent.ww2_historical_weapons_mod.client.renderer.StickGrenadeRenderer;
import com.apphysicsstudent.ww2_historical_weapons_mod.init.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "ww2_historical_weapons_mod", value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Changed this back to  custom StickGrenadeRenderer
        event.registerEntityRenderer(ModEntities.STICK_GRENADE.get(), StickGrenadeRenderer::new);
    }
}
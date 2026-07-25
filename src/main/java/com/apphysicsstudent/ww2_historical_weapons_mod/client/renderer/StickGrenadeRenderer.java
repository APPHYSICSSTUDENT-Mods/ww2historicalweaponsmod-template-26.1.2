package com.apphysicsstudent.ww2_historical_weapons_mod.client.renderer;

import com.apphysicsstudent.ww2_historical_weapons_mod.entity.StickGrenadeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState; // <-- The correct 26.1.2 package path!
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class StickGrenadeRenderer extends EntityRenderer<StickGrenadeEntity, StickGrenadeRenderer.StickGrenadeRenderState> {

    private final ItemModelResolver itemModelResolver;

    public StickGrenadeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public StickGrenadeRenderState createRenderState() {
        return new StickGrenadeRenderState();
    }

    @Override
    public void extractRenderState(StickGrenadeEntity entity, StickGrenadeRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        // Smoothly calculate the spin using Minecraft's built-in math interpolator
        state.spin = net.minecraft.util.Mth.lerp(partialTick, entity.prevSpin, entity.currentSpin);

        this.itemModelResolver.updateForTopItem(
                state.itemState,
                entity.getItem(),
                ItemDisplayContext.GROUND,
                entity.level(),
                entity,
                entity.getId()
        );
    }

    // Changing the 4th argument to the required CameraRenderState
    @Override
    public void submit(StickGrenadeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(state.spin));

        // Since the outer method no longer passes packedLight, we provide a default bright value here
        state.itemState.submit(
                poseStack,
                collector,
                15728880, // Default full-bright packed light
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();

        // Pass the required CameraRenderState up to the superclass
        super.submit(state, poseStack, collector, cameraState);
    }

    public static class StickGrenadeRenderState extends EntityRenderState {
        public float spin;
        public final ItemStackRenderState itemState = new ItemStackRenderState();
    }
}
package com.apphysicsstudent.ww2_historical_weapons_mod.item;

import com.apphysicsstudent.ww2_historical_weapons_mod.WW2Historicalweaponsmod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ZB26Item extends Item {
    private static final int MAX_AMMO = 20;

    public ZB26Item(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack gunStack = player.getItemInHand(hand);
        int roundsFired = gunStack.getDamageValue();
        int currentAmmo = MAX_AMMO - roundsFired;

        // --- RELOAD LOGIC: Sneak + Right-Click ---
        if (player.isCrouching()) {
            if (currentAmmo == 0) {
                int magSlot = findItemInInventory(player, WW2Historicalweaponsmod.LOADED_ZB26_MAGAZINE.get());

                if (magSlot != -1 || player.getAbilities().instabuild) {
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().getItem(magSlot).shrink(1);
                        player.getInventory().add(new ItemStack(WW2Historicalweaponsmod.ZB26_MAGAZINE.get()));
                    }

                    gunStack.setDamageValue(0);

                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 1.0F, 1.0F);

                    if (!level.isClientSide()) {
                        player.sendSystemMessage(Component.literal("Loaded 20-Round ZB-26 Magazine!"));
                    }
                    return InteractionResult.CONSUME;
                } else if (!level.isClientSide()) {
                    player.sendSystemMessage(Component.literal("No Loaded ZB-26 Magazines in inventory!"));
                }
            } else if (!level.isClientSide()) {
                player.sendSystemMessage(Component.literal("Magazine already inserted! (" + currentAmmo + "/20)"));
            }
            return InteractionResult.FAIL;
        }

        // --- FIRING LOGIC: Regular Right-Click ---
        if (currentAmmo > 0 || player.getAbilities().instabuild) {
            if (!player.getAbilities().instabuild) {
                gunStack.setDamageValue(roundsFired + 1);
            }

            player.getCooldowns().addCooldown(gunStack, 5);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.8F, 1.5F);

            shootHitscan(level, player, 16.0F, 100.0D);

            return InteractionResult.CONSUME;
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.2F);
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.literal("Out of ammo! Crouch + Right-Click with a loaded magazine."));
            }
        }

        return InteractionResult.FAIL;
    }

    private int findItemInInventory(Player player, Item item) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (player.getInventory().getItem(i).getItem() == item) return i;
        }
        return -1;
    }

    // @Override removed to prevent strict signature mismatch compiler errors
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int currentAmmo = MAX_AMMO - stack.getDamageValue();
        tooltip.add(Component.literal("Ammo: " + currentAmmo + "/" + MAX_AMMO));
        tooltip.add(Component.literal("Sneak + Right-Click: Load ZB-26 Magazine"));
    }

    private void shootHitscan(Level level, Player player, float damage, double maxRange) {
        if (level.isClientSide()) return;

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 maxReachVec = eyePos.add(lookVec.scale(maxRange));

        BlockHitResult blockHit = level.clip(new ClipContext(
                eyePos,
                maxReachVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        Vec3 targetPos = (blockHit.getType() != HitResult.Type.MISS) ? blockHit.getLocation() : maxReachVec;
        AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(maxRange)).inflate(1.0D);

        // Updated for correct 26.1.2 signature parameters
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player,
                eyePos,
                targetPos,
                searchBox,
                target -> !target.isSpectator() && target.isPickable(),
                0.0D
        );

        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            livingTarget.hurt(player.damageSources().playerAttack(player), damage);
            targetPos = entityHit.getLocation();
        }

        //  muzzle flash particle right at the gun barrel
        if (level instanceof ServerLevel serverLevel) {
            Vec3 barrelPos = eyePos.add(lookVec.scale(0.8D)); // Position slightly in front of player
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    barrelPos.x, barrelPos.y, barrelPos.z,
                    3, 0.05, 0.05, 0.05, 0.01
            );
        }
    }
}
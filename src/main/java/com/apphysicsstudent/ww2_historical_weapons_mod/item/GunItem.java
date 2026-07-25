package com.apphysicsstudent.ww2_historical_weapons_mod.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GunItem extends Item {
    private final float minDamage;
    private final float maxDamage;
    private final int cooldownTicks;
    private final double range;

    public GunItem(Properties properties, float minDamage, float maxDamage, int cooldownTicks, double range) {
        super(properties);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.cooldownTicks = cooldownTicks;
        this.range = range;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // Only calculate shooting logic on the server side
        if (!level.isClientSide()) {
            // 1. Play gun firing sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.8F, 1.8F);

            // 2. Perform Hitscan / Raycast
            shootHitscan(level, player);

            // 3. Set firing cooldown using the item stack in hand
            player.getCooldowns().addCooldown(itemstack, this.cooldownTicks);
        }

        return InteractionResult.SUCCESS;
    }

    private void shootHitscan(Level level, Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(this.range));

        // Check for block collisions (bullets stop at walls)
        HitResult blockHit = level.clip(new ClipContext(
                start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        // Check for entity collisions along the ray
        AABB searchBox = player.getBoundingBox().expandTowards(look.scale(this.range)).inflate(1.0);

        // 26.1.2 method signature: (shooter, start, end, searchBox, filter, distanceSquared)
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player, start, end, searchBox,
                entity -> !entity.isSpectator() && entity.isPickable(),
                this.range * this.range
        );

        // Deal randomized damage if an entity was struck
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
            float damage = this.minDamage + level.getRandom().nextFloat() * (this.maxDamage - this.minDamage);
            target.hurt(level.damageSources().playerAttack(player), damage);
        }
    }
}
package com.apphysicsstudent.ww2_historical_weapons_mod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class StickGrenadeEntity extends ThrowableItemProjectile {
    private int fuse = 80; // 80 ticks = 4 seconds
    public float currentSpin = 0.0F;
    public float prevSpin = 0.0F;
    public boolean hasLanded = false;

    public StickGrenadeEntity(EntityType<? extends StickGrenadeEntity> type, Level level) {
        super(type, level);
    }

    public StickGrenadeEntity(EntityType<? extends StickGrenadeEntity> type, LivingEntity shooter, Level level) {
        // Only use the available 2-parameter constructor
        super(type, level);

        // Manually assign the shooter's data to the projectile
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TNT;
    }
    @Override
    public void tick() {
        super.tick();

        this.prevSpin = this.currentSpin;

        // Only spin if we haven't hit a block yet
        if (!this.hasLanded) {
            this.currentSpin += 35.0F;
        }

        if (this.level().isClientSide()) {
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, this.getX(), this.getY() + 0.2D, this.getZ(), 0.0D, 0.02D, 0.0D);
        }

        this.fuse--;
        if (this.fuse <= 0) {
            this.explode();
        }
    }

    @Override
    protected void onHitBlock(net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);

        // The absolute microsecond it touches a block, lock the rotation!
        this.hasLanded = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level().isClientSide()) {
            entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 2.0F);

            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x * -0.2D, motion.y * -0.1D, motion.z * -0.2D);
        }
    }

    private void explode() {
        if (!this.level().isClientSide()) {
            this.level().explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    3.5F,
                    Level.ExplosionInteraction.TNT
            );
            this.discard();
        }
    }
}
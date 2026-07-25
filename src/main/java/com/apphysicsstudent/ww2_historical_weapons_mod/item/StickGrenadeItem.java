package com.apphysicsstudent.ww2_historical_weapons_mod.item;

import com.apphysicsstudent.ww2_historical_weapons_mod.entity.StickGrenadeEntity;
import com.apphysicsstudent.ww2_historical_weapons_mod.init.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StickGrenadeItem extends Item {

    public StickGrenadeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // 1. Play fuse pull / throwing sound (hissing sound)
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.TNT_PRIMED,
                SoundSource.PLAYERS,
                0.6F, 1.2F
        );

        // 2. Spawn grenade on server side
        if (!level.isClientSide()) {
            StickGrenadeEntity grenade = new StickGrenadeEntity(ModEntities.STICK_GRENADE.get(), player, level);

            // Pass current stack to entity so renderer knows what item model to draw
            grenade.setItem(itemstack);

            // 0.8F velocity = heavy lobbed throw (snowballs use 1.5F)
            grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.8F, 1.0F);

            level.addFreshEntity(grenade);
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        // 3. Consume 1 grenade if not in creative
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
package com.apphysicsstudent.ww2_historical_weapons_mod;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("ww2_historical_weapons_mod");

    // Your first weapon!
    public static final DeferredItem<Item> ZB_26 = ITEMS.register("zb_26",
            () -> new Item(new Item.Properties()));
}
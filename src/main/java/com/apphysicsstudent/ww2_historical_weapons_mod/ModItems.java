package com.apphysicsstudent.ww2_historical_weapons_mod;

import com.apphysicsstudent.ww2_historical_weapons_mod.item.Type11Item;
import com.apphysicsstudent.ww2_historical_weapons_mod.item.ZB26Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("ww2_historical_weapons_mod");

    // Register the ZB-26 using its CUSTOM class
    public static final DeferredItem<Item> ZB_26 = ITEMS.register("zb_26",
            () -> new ZB26Item(new Item.Properties()));

    // Register the Type 11 using its CUSTOM class
    public static final DeferredItem<Item> TYPE_11_LMG = ITEMS.register("type_11",
            () -> new Type11Item(new Item.Properties()));
// and other items
    public static final DeferredItem<Item> ZB26_MAGAZINE = ITEMS.register("zb_26_magazine_empty",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LOADED_ZB26_MAGAZINE = ITEMS.register("loaded_zb26_magazine",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> REDSTONE_NUGGET = ITEMS.register("redstone_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FIVE_ROUND_CLIP_65 = ITEMS.register("five_round_clip_65",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STICK_GRENADE = ITEMS.register("stick_grenade",
            () -> new Item(new Item.Properties()));
}
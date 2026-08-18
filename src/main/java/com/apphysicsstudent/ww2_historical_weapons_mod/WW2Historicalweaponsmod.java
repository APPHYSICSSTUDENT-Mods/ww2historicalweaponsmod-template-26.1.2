package com.apphysicsstudent.ww2_historical_weapons_mod;

import com.apphysicsstudent.ww2_historical_weapons_mod.init.ModEntities;
import com.apphysicsstudent.ww2_historical_weapons_mod.item.Type11Item;
import com.apphysicsstudent.ww2_historical_weapons_mod.item.ZB26Item;
import com.apphysicsstudent.ww2_historical_weapons_mod.item.StickGrenadeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(WW2Historicalweaponsmod.MODID)
public class WW2Historicalweaponsmod {

    public static final String MODID = "ww2_historical_weapons_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredItem<Item> STICK_GRENADE = ITEMS.registerItem("stick_grenade",
            properties -> new StickGrenadeItem(properties.stacksTo(16)));
    public static final DeferredItem<Item> ZB_26 = ITEMS.registerItem("zb_26",
            properties -> new ZB26Item(properties.stacksTo(1).durability(20)));

    public static final DeferredItem<Item> TYPE_11 = ITEMS.registerItem("type_11",
            properties -> new Type11Item(properties.stacksTo(1).durability(30)));;
    // --- INGREDIENTS & NUGGETS ---
    public static final DeferredItem<Item> GUNPOWDER_NUGGET = ITEMS.registerItem("gunpowder_nugget",
            Item::new);

    public static final DeferredItem<Item> REDSTONE_NUGGET = ITEMS.registerItem("redstone_nugget",
            Item::new);

    // --- AMMO & CLIPS ---
    public static final DeferredItem<Item> AMMO_6_5_MM = ITEMS.registerItem("ammo_6_5_mm",
            Item::new);

    public static final DeferredItem<Item> ZB26_ROUND = ITEMS.registerItem("zb26_round",
            Item::new);

    public static final DeferredItem<Item> FIVE_ROUND_CLIP_65 = ITEMS.registerItem("five_round_clip_65",
            Item::new);

    public static final DeferredItem<Item> TWENTY_ROUND_CLIP = ITEMS.registerItem("twenty_round_clip",
            Item::new);

    // --- MAGAZINES ---
    public static final DeferredItem<Item> ZB26_MAGAZINE = ITEMS.registerItem("zb26_magazine",
            properties -> new Item(properties.stacksTo(1)));

    public static final DeferredItem<Item> LOADED_ZB26_MAGAZINE = ITEMS.registerItem("loaded_zb26_magazine",
            properties -> new Item(properties.stacksTo(1)));
    // 2. Register Creative Tab & Add Items to Output
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("ww2_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ww2_historical_weapons_mod"))
                    .icon(() -> ZB_26.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {

                        // Guns & Grenades
                        output.accept(STICK_GRENADE.get());
                        output.accept(ZB_26.get());
                        output.accept(TYPE_11.get());

                        // Crafting Nuggets
                        output.accept(GUNPOWDER_NUGGET.get());
                        output.accept(REDSTONE_NUGGET.get());

                        // Single Rounds
                        output.accept(AMMO_6_5_MM.get());
                        output.accept(ZB26_ROUND.get());

                        // Clips & Magazines
                        output.accept(FIVE_ROUND_CLIP_65.get());
                        output.accept(TWENTY_ROUND_CLIP.get());
                        output.accept(ZB26_MAGAZINE.get());
                        output.accept(LOADED_ZB26_MAGAZINE.get());

                    })
                    .build()
    );
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Check if the tab being built is the Combat tab
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            // Add grenade to the tab!
            event.accept(STICK_GRENADE.get());
        }
    }

    public WW2Historicalweaponsmod(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModEntities.register(modEventBus);
        modEventBus.addListener(this::addCreative);

    }
}
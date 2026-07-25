package com.apphysicsstudent.ww2_historical_weapons_mod;

import com.apphysicsstudent.ww2_historical_weapons_mod.init.ModEntities;
import com.apphysicsstudent.ww2_historical_weapons_mod.item.GunItem;
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
    // ZB-26: 12.0 to 20.0 Damage Points, 5-tick cooldown, 80 block range
    public static final DeferredItem<Item> ZB_26 = ITEMS.registerItem("zb_26",
            properties -> new GunItem(properties.stacksTo(1), 14.0F, 25.0F, 5, 80.0));

    // Type 11: 10.0 to 14.0 Damage Points, 4-tick cooldown, 70 block range
    public static final DeferredItem<Item> TYPE_11 = ITEMS.registerItem("type_11",
            properties -> new GunItem(properties.stacksTo(1), 12.0F, 17.0F, 4, 70.0));
    // 2. Register Creative Tab & Add Items to Output
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("ww2_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ww2_historical_weapons_mod"))
                    .icon(() -> ZB_26.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {

                        output.accept(ZB_26.get()); // <--- PUT IT RIGHT HERE!

                    })
                    .build()
    );
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Check if the tab being built is the Combat tab
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            // Add your grenade to the tab! (Make sure to reference wherever your item is registered)
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
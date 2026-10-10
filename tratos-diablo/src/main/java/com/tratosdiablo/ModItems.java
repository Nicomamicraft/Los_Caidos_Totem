package com.tratosdiablo;

import java.util.EnumMap;
import java.util.Map;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class ModItems {
    public static final Map<DevilTier, DevilTotemItem> TOTEMS = new EnumMap<>(DevilTier.class);

    private ModItems() {}

    public static void register() {
        for (DevilTier tier : DevilTier.values()) {
            DevilTotemItem item = new DevilTotemItem(tier, new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
            Registry.register(Registries.ITEM, Identifier.of(TratosDiabloMod.MOD_ID, tier.id), item);
            TOTEMS.put(tier, item);
        }
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries ->
                TOTEMS.values().forEach(entries::add));
    }
}

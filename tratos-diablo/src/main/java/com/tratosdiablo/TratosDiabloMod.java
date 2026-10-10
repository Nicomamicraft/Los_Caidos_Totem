package com.tratosdiablo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;

public class TratosDiabloMod implements ModInitializer {
    public static final String MOD_ID = "tratos_diablo";

    @Override
    public void onInitialize() {
        ModItems.register();

        // Salvar de la muerte (igual que el tótem vanilla, que tiene prioridad si lo llevas)
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return true;
            return !tryActivate(player, source);
        });

        // Bonos mientras el tótem está en la mano
        ServerTickEvents.END_SERVER_TICK.register(server ->
                server.getPlayerManager().getPlayerList().forEach(TratosDiabloMod::updateHeldBonuses));
    }

    /** @return true si un tótem se activó y la muerte debe cancelarse. */
    private static boolean tryActivate(ServerPlayerEntity player, DamageSource source) {
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;

        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (!(stack.getItem() instanceof DevilTotemItem totem)) continue;

            ItemStack copy = stack.copy();
            stack.decrement(1);
            player.incrementStat(Stats.USED.getOrCreateStat(totem));
            Criteria.USED_TOTEM.trigger(player, copy);
            player.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);

            applyPermanent(player, totem.tier());

            player.setHealth(1.0F);
            player.clearStatusEffects();
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            player.getWorld().sendEntityStatus(player, (byte) 35); // animación del tótem
            return true;
        }
        return false;
    }

    private static boolean isHolding(ServerPlayerEntity player, DevilTier tier) {
        for (Hand hand : Hand.values()) {
            if (player.getStackInHand(hand).getItem() instanceof DevilTotemItem t && t.tier() == tier) return true;
        }
        return false;
    }

    private static void updateHeldBonuses(ServerPlayerEntity player) {
        for (DevilTier tier : DevilTier.values()) {
            if (tier.held.isEmpty()) continue;
            boolean holding = isHolding(player, tier);
            tier.held.forEach((key, attribute, value) -> {
                EntityAttributeInstance inst = player.getAttributeInstance(attribute);
                if (inst == null) return;
                Identifier id = Identifier.of(MOD_ID, "held_" + tier.id + "_" + key);
                if (holding && !inst.hasModifier(id)) {
                    inst.addTemporaryModifier(new EntityAttributeModifier(id, value, EntityAttributeModifier.Operation.ADD_VALUE));
                } else if (!holding && inst.hasModifier(id)) {
                    inst.removeModifier(id);
                }
            });
        }
        if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
    }

    /** Bonos permanentes (se guardan con el jugador y se acumulan si se activa varias veces). */
    private static void applyPermanent(ServerPlayerEntity player, DevilTier tier) {
        tier.activation.forEach((key, attribute, value) -> {
            EntityAttributeInstance inst = player.getAttributeInstance(attribute);
            if (inst == null) return;
            Identifier id = Identifier.of(MOD_ID, "perm_" + tier.id + "_" + key);
            double total = value;
            EntityAttributeModifier old = inst.getModifier(id);
            if (old != null) {
                total += old.value();
                inst.removeModifier(id);
            }
            inst.addPersistentModifier(new EntityAttributeModifier(id, total, EntityAttributeModifier.Operation.ADD_VALUE));
        });
    }
}

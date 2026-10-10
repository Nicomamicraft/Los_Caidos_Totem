package com.tratosdiablo;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;

/** Bonos de atributos (todos con operación ADD_VALUE). */
public record Bonus(double maxHealth, double armor, double speed, double knockback, double attack) {

    public static final Bonus NONE = new Bonus(0, 0, 0, 0, 0);

    public interface Visitor {
        void accept(String key, RegistryEntry<EntityAttribute> attribute, double value);
    }

    public boolean isEmpty() {
        return maxHealth == 0 && armor == 0 && speed == 0 && knockback == 0 && attack == 0;
    }

    public void forEach(Visitor v) {
        if (maxHealth != 0) v.accept("max_health", EntityAttributes.GENERIC_MAX_HEALTH, maxHealth);
        if (armor != 0) v.accept("armor", EntityAttributes.GENERIC_ARMOR, armor);
        if (speed != 0) v.accept("speed", EntityAttributes.GENERIC_MOVEMENT_SPEED, speed);
        if (knockback != 0) v.accept("knockback", EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, knockback);
        if (attack != 0) v.accept("attack", EntityAttributes.GENERIC_ATTACK_DAMAGE, attack);
    }
}

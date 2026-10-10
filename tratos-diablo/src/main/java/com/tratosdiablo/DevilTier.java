package com.tratosdiablo;

import net.minecraft.util.Formatting;

public enum DevilTier {
    // held = bonos mientras lo llevas en la mano / offhand
    // activation = bonos permanentes que se suman al activarse el tótem
    COMUN("trato_comun", Formatting.GRAY,
            Bonus.NONE, Bonus.NONE,
            "Es como un tótem de la inmortalidad normal"),

    AVANZADO("trato_avanzado", Formatting.GOLD,
            new Bonus(4, 4, 0.02, 0, 0),
            new Bonus(4, 4, 0.02, 0, 0),
            "Una segunda oportunidad, pero con un coste"),

    SUPREMO("trato_supremo", Formatting.RED,
            new Bonus(10, 6, 0.03, 0.5, 0),
            new Bonus(10, 6, 0.03, 5, 0),
            "Has recibido un gran poder"),

    LEGENDARIO("trato_legendario", Formatting.LIGHT_PURPLE,
            new Bonus(20, 10, 0.05, 1.0, 1),
            new Bonus(20, 10, 0.05, 10, 1),
            "Has vendido tu alma por completo al diablo");

    public final String id;
    public final Formatting color;
    public final Bonus held;
    public final Bonus activation;
    public final String lore;

    DevilTier(String id, Formatting color, Bonus held, Bonus activation, String lore) {
        this.id = id;
        this.color = color;
        this.held = held;
        this.activation = activation;
        this.lore = lore;
    }
}

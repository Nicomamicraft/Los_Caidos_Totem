package com.tratosdiablo;

import java.math.BigDecimal;
import java.util.List;

import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DevilTotemItem extends Item {
    private final DevilTier tier;

    public DevilTotemItem(DevilTier tier, Settings settings) {
        super(settings);
        this.tier = tier;
    }

    public DevilTier tier() {
        return tier;
    }

    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().formatted(tier.color);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return tier != DevilTier.COMUN;
    }

    private static String n(double v) {
        return BigDecimal.valueOf(v).stripTrailingZeros().toPlainString();
    }

    private static Text line(String s, Formatting f) {
        return Text.literal(s).formatted(f);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Bonus h = tier.held;
        if (h.maxHealth() != 0) tooltip.add(line("+" + n(h.maxHealth() / 2) + " Corazones de vida", Formatting.RED));
        if (h.armor() != 0) tooltip.add(line("+" + n(h.armor()) + " Armadura", Formatting.WHITE));
        if (h.speed() != 0) tooltip.add(line("+" + n(h.speed() / 0.1 * 100) + "% Velocidad", Formatting.AQUA));
        if (h.knockback() != 0) {
            double pct = Math.min(100, h.knockback() * 100);
            tooltip.add(line(n(pct) + (pct >= 100 ? "% Inmunidad al knockback" : "% Resistencia al knockback"), Formatting.YELLOW));
        }
        if (h.attack() != 0) tooltip.add(line("+" + n(h.attack()) + " Daño de ataque", Formatting.RED));

        tooltip.add(Text.empty());
        tooltip.add(line(tier.lore, tier.color));

        Bonus a = tier.activation;
        if (!a.isEmpty()) {
            tooltip.add(line("Al activarse:", Formatting.GRAY));
            if (a.maxHealth() != 0) tooltip.add(line("+" + n(a.maxHealth()) + " Max Health", Formatting.BLUE));
            if (a.armor() != 0) tooltip.add(line("+" + n(a.armor()) + " Armor", Formatting.BLUE));
            if (a.speed() != 0) tooltip.add(line("+" + n(a.speed()) + " Speed", Formatting.BLUE));
            if (a.knockback() != 0) tooltip.add(line("+" + n(a.knockback()) + " Knockback Resistance", Formatting.BLUE));
            if (a.attack() != 0) tooltip.add(line("+" + n(a.attack()) + " Attack Damage", Formatting.BLUE));
        }
    }
}

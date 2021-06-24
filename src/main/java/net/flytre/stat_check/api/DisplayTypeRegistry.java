package net.flytre.stat_check.api;

import com.google.common.collect.ImmutableList;
import net.flytre.flytre_lib.common.util.ItemUtils;
import net.flytre.stat_check.mixin.RegistryInvoker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

import static net.flytre.stat_check.api.DisplayType.DEFAULT_TEXTURE;
import static net.minecraft.entity.attribute.EntityAttributes.*;

public class DisplayTypeRegistry {

    public static final RegistryKey<Registry<DisplayType<?>>> DISPLAY_TYPE_KEY = RegistryKey.ofRegistry(new Identifier("stat_check", "display_type"));
    public static final Registry<DisplayType<?>> DISPLAY_TYPE_REGISTRY = RegistryInvoker.createRegistry(DISPLAY_TYPE_KEY, () -> DisplayType.EMPTY);
    private static final List<DisplayType<?>> VALUES = new ArrayList<>();

    static {
        DisplayTypeRegistry.register(new Identifier("stat_check", "durability"), DisplayType.ofDouble(DEFAULT_TEXTURE, 0, ItemStack::isDamageable, stack -> (double) stack.getMaxDamage()));
        DisplayTypeRegistry.register(new Identifier("stat_check", "attack_damage"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 3, GENERIC_ATTACK_DAMAGE));
        DisplayTypeRegistry.register(new Identifier("stat_check", "attack_speed"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 5, GENERIC_ATTACK_SPEED));
        DisplayTypeRegistry.register(new Identifier("stat_check", "attack_knockback"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 10, GENERIC_ATTACK_KNOCKBACK));
        DisplayTypeRegistry.register(new Identifier("stat_check", "harvest_level"), DisplayType.ofDouble(DEFAULT_TEXTURE, 1, stack -> stack.getItem() instanceof ToolItem, stack -> (double) ItemUtils.getHarvestLevel(stack)));
        DisplayTypeRegistry.register(new Identifier("stat_check", "harvest_speed"), DisplayType.ofDouble(DEFAULT_TEXTURE, 2, stack -> ItemUtils.getHarvestSpeed(stack) != -1, ItemUtils::getHarvestSpeed));
        DisplayTypeRegistry.register(new Identifier("stat_check", "armor"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 6, GENERIC_ARMOR));
        DisplayTypeRegistry.register(new Identifier("stat_check", "armor_toughness"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 7, GENERIC_ARMOR_TOUGHNESS));
        DisplayTypeRegistry.register(new Identifier("stat_check", "knockback_resistance"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 4, GENERIC_KNOCKBACK_RESISTANCE));
        DisplayTypeRegistry.register(new Identifier("stat_check", "max_health"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 11, GENERIC_MAX_HEALTH));
        DisplayTypeRegistry.register(new Identifier("stat_check", "movement_speed"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 9, GENERIC_MOVEMENT_SPEED));
        DisplayTypeRegistry.register(new Identifier("stat_check", "enchantability"), DisplayType.ofDouble(DEFAULT_TEXTURE, 8, stack -> stack.getItem().getEnchantability() > 0, stack -> (double) stack.getItem().getEnchantability()));
        DisplayTypeRegistry.register(new Identifier("stat_check", "luck"), DisplayType.ofAttribute(DEFAULT_TEXTURE, 12, GENERIC_LUCK));
        DisplayTypeRegistry.register(new Identifier("stat_check", "hunger"), DisplayType.ofDouble(DEFAULT_TEXTURE, 17, ItemStack::isFood, stack -> (double) stack.getItem().getFoodComponent().getHunger()));
        DisplayTypeRegistry.register(new Identifier("stat_check", "saturation"), DisplayType.ofDouble(DEFAULT_TEXTURE, 16, ItemStack::isFood, stack -> (double) (stack.getItem().getFoodComponent().getSaturationModifier() * 10)));
        DisplayTypeRegistry.register(new Identifier("stat_check", "hardness"), DisplayType.ofDouble(DEFAULT_TEXTURE, 18, DisplayType::blockItemButNotFoodItem, stack -> (double) ItemUtils.getHardness(stack)));
        DisplayTypeRegistry.register(new Identifier("stat_check", "blast_resistance"), DisplayType.ofDouble(DEFAULT_TEXTURE, 19, DisplayType::blockItemButNotFoodItem, stack -> (double) ItemUtils.getResistance(stack)));
        DisplayTypeRegistry.register(new Identifier("stat_check", "luminance"), DisplayType.ofDouble(DEFAULT_TEXTURE, 20, stack -> ItemUtils.getLuminance(stack.getItem()).isPresent(), stack -> Double.valueOf(ItemUtils.getLuminance(stack.getItem()).orElse(0))));
        DisplayTypeRegistry.register(new Identifier("stat_check", "flammability"), DisplayType.ofTranslatableString(DEFAULT_TEXTURE, 21, DisplayType::blockItemButNotFoodItem, stack -> ItemUtils.getFlammable(stack) ? "gui.yes" : "gui.no"));
    }

    public static <T> DisplayType<T> register(Identifier id, DisplayType<T> entry) {
        DisplayType<T> type = Registry.register(DISPLAY_TYPE_REGISTRY, id, entry);
        VALUES.add(type);
        return type;
    }

    public static List<DisplayType<?>> values() {
        return ImmutableList.copyOf(VALUES);
    }
}

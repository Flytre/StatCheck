package net.flytre.stat_check.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.flytre.flytre_lib.common.util.ItemUtils;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.minecraft.entity.attribute.EntityAttributes.*;

public class DisplayType<T> implements Comparable<DisplayType<?>> {


    private static final Set<EntityAttribute> COVERED_ATTRIBUTES = new HashSet<>();
    private static final List<DisplayType<?>> VALUES = new ArrayList<>();
    private static final DisplayType<Double> DURABILITY = DisplayType.ofDouble(0, ItemStack::isDamageable, stack -> (double) stack.getMaxDamage());
    private static final DisplayType<Double> ATTACK_DAMAGE = DisplayType.ofAttribute(3, GENERIC_ATTACK_DAMAGE);
    private static final DisplayType<Double> ATTACK_SPEED = DisplayType.ofAttribute(5, GENERIC_ATTACK_SPEED);
    private static final DisplayType<Double> ATTACK_KNOCKBACK = DisplayType.ofAttribute(10, GENERIC_ATTACK_KNOCKBACK);
    private static final DisplayType<Double> HARVEST_LEVEL = DisplayType.ofDouble(1, stack -> stack.getItem() instanceof ToolItem, stack -> (double) ItemUtils.getHarvestLevel(stack));
    private static final DisplayType<Double> HARVEST_SPEED = DisplayType.ofDouble(2, stack -> ItemUtils.getHarvestSpeed(stack) != -1, ItemUtils::getHarvestSpeed);
    private static final DisplayType<Double> ARMOR = DisplayType.ofAttribute(6, GENERIC_ARMOR);
    private static final DisplayType<Double> ARMOR_TOUGHNESS = DisplayType.ofAttribute(7, GENERIC_ARMOR_TOUGHNESS);
    private static final DisplayType<Double> KNOCKBACK_RESISTANCE = DisplayType.ofAttribute(4, GENERIC_KNOCKBACK_RESISTANCE);
    private static final DisplayType<Double> MAX_HEALTH = DisplayType.ofAttribute(11, GENERIC_MAX_HEALTH);
    private static final DisplayType<Double> MOVEMENT_SPEED = DisplayType.ofAttribute(9, GENERIC_MOVEMENT_SPEED);
    private static final DisplayType<Double> ENCHANTABILITY = DisplayType.ofDouble(8, stack -> stack.getItem().getEnchantability() > 0, stack -> (double) stack.getItem().getEnchantability());
    private static final DisplayType<Double> LUCK = DisplayType.ofAttribute(12, GENERIC_LUCK);
    private static final DisplayType<Double> HUNGER = DisplayType.ofDouble(17, ItemStack::isFood, stack -> (double) stack.getItem().getFoodComponent().getHunger());
    private static final DisplayType<Double> SATURATION = DisplayType.ofDouble(16, ItemStack::isFood, stack -> (double) (stack.getItem().getFoodComponent().getSaturationModifier() * 10));
    private static final DisplayType<Double> HARDNESS = DisplayType.ofDouble(18, DisplayType::blockItemButNotFoodItem, stack -> (double) ItemUtils.getHardness(stack));
    private static final DisplayType<Double> RESISTANCE = DisplayType.ofDouble(19, DisplayType::blockItemButNotFoodItem, stack -> (double) ItemUtils.getResistance(stack));
    private static final DisplayType<Double> LUMINANCE = DisplayType.ofDouble(20, stack -> ItemUtils.getLuminance(stack.getItem()).isPresent(), stack -> Double.valueOf(ItemUtils.getLuminance(stack.getItem()).orElse(0)));
    private static final DisplayType<String> FLAMMABLE = DisplayType.ofTranslatableString(21, DisplayType::blockItemButNotFoodItem, stack -> ItemUtils.getFlammable(stack) ? "gui.yes" : "gui.no");
    private final int iconIndex;
    private final Predicate<ItemStack> display;
    private final Function<ItemStack, StatEntry<T>> value;
    private final int index;
    public DisplayType(int iconIndex, Predicate<ItemStack> display, Function<ItemStack, StatEntry<T>> value) {
        this.iconIndex = iconIndex;
        this.display = display;
        this.value = value;
        index = VALUES.size();
        VALUES.add(this);
    }

    public static boolean blockItemButNotFoodItem(ItemStack stack) {
        return stack.getItem() instanceof BlockItem && !stack.isFood();
    }


    public static Set<EntityAttribute> getCoveredAttributes() {
        return ImmutableSet.copyOf(COVERED_ATTRIBUTES);
    }

    public static List<DisplayType<?>> values() {
        return ImmutableList.copyOf(VALUES);
    }
    public static DisplayType<Double> ofAttribute(int index, EntityAttribute attribute) {
        COVERED_ATTRIBUTES.add(attribute);
        return ofDouble(index, stack -> ItemUtils.hasAttribute(stack, attribute), stack -> ItemUtils.getAttributeValue(stack, attribute));
    }

    public static DisplayType<Double> ofDouble(int index, Predicate<ItemStack> display, Function<ItemStack, Double> value) {
        Function<ItemStack, StatEntry<Double>> entry = stack -> new StatEntry.DoubleEntry(value.apply(stack));
        return new DisplayType<>(index, display, entry);
    }


    public static DisplayType<String> ofString(int index, Predicate<ItemStack> display, Function<ItemStack, String> value) {
        Function<ItemStack, StatEntry<String>> entry = stack -> new StatEntry.StringEntry(value.apply(stack));
        return new DisplayType<>(index, display, entry);
    }

    public static DisplayType<String> ofTranslatableString(int index, Predicate<ItemStack> display, Function<ItemStack, String> value) {
        Function<ItemStack, StatEntry<String>> entry = stack -> new StatEntry.TranslatableEntry(value.apply(stack));
        return new DisplayType<>(index, display, entry);
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public boolean shouldDisplay(ItemStack stack) {
        return display.test(stack);
    }

    public StatEntry<T> getValue(ItemStack stack) {
        return value.apply(stack);
    }

    @Override
    public int compareTo(@NotNull DisplayType<?> o) {
        return Integer.compare(index, o.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayType<?> that = (DisplayType<?>) o;

        return index == that.index;
    }

    @Override
    public int hashCode() {
        return index;
    }
}

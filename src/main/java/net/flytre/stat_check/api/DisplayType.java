package net.flytre.stat_check.api;

import net.flytre.flytre_lib.common.util.ItemUtils;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public class DisplayType<T> implements Comparable<DisplayType<?>> {


    public static final Identifier DEFAULT_TEXTURE = new Identifier("stat_check", "textures/gui/hud.png");
    public static DisplayType<String> EMPTY = DisplayType.ofString(DEFAULT_TEXTURE, 63, i -> false, i -> "");
    private static int INDEX = 0;

    private final int iconIndex;
    private final Predicate<ItemStack> display;
    private final Function<ItemStack, StatEntry<T>> value;
    private final int index;
    private final Identifier texture;
    private final @Nullable EntityAttribute attribute;

    public DisplayType(Identifier texture, int iconIndex, Predicate<ItemStack> display, Function<ItemStack, StatEntry<T>> value) {
        this(texture, iconIndex, display, value, null);
    }

    public DisplayType(Identifier texture, int iconIndex, Predicate<ItemStack> display, Function<ItemStack, StatEntry<T>> value, EntityAttribute attribute) {
        this.texture = texture;
        this.iconIndex = iconIndex;
        this.display = display;
        this.value = value;
        this.index = INDEX++;
        this.attribute = attribute;
    }

    public @Nullable EntityAttribute getAttribute() {
        return attribute;
    }

    public static boolean blockItemButNotFoodItem(ItemStack stack) {
        return stack.getItem() instanceof BlockItem && !stack.isFood();
    }


    public static DisplayType<Double> ofAttribute(Identifier texture, int index, EntityAttribute attribute) {
        Function<ItemStack, StatEntry<Double>> entry = stack -> new StatEntry.DoubleEntry(ItemUtils.getAttributeValue(stack, attribute));
        return new DisplayType<>(texture, index, stack -> ItemUtils.hasAttribute(stack, attribute), entry, attribute);
    }

    public static DisplayType<Double> ofDouble(Identifier texture, int index, Predicate<ItemStack> display, Function<ItemStack, Double> value) {
        Function<ItemStack, StatEntry<Double>> entry = stack -> new StatEntry.DoubleEntry(value.apply(stack));
        return new DisplayType<>(texture, index, display, entry);
    }

    public static DisplayType<String> ofString(Identifier texture, int index, Predicate<ItemStack> display, Function<ItemStack, String> value) {
        Function<ItemStack, StatEntry<String>> entry = stack -> new StatEntry.StringEntry(value.apply(stack));
        return new DisplayType<>(texture, index, display, entry);
    }

    public static DisplayType<String> ofTranslatableString(Identifier texture, int index, Predicate<ItemStack> display, Function<ItemStack, String> value) {
        Function<ItemStack, StatEntry<String>> entry = stack -> new StatEntry.TranslatableEntry(value.apply(stack));
        return new DisplayType<>(texture, index, display, entry);
    }

    public Identifier getTexture() {
        return texture;
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

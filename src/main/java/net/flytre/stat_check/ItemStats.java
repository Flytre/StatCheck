package net.flytre.stat_check;

import com.google.common.collect.ImmutableMap;
import net.flytre.stat_check.api.DisplayType;
import net.flytre.stat_check.api.StatEntry;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.TreeMap;


public class ItemStats {


    private final Map<DisplayType<?>, StatEntry<?>> values;
    private final ItemStack stack;

    public ItemStats(ItemStack stack) {
        this.stack = stack;
        Map<DisplayType<?>, StatEntry<?>> values = new TreeMap<>();

        for (DisplayType<?> type : DisplayType.values())
            if (type.shouldDisplay(stack))
                values.put(type, type.getValue(stack));

        this.values = ImmutableMap.copyOf(values);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStats itemStats = (ItemStats) o;

        if (!values.equals(itemStats.values)) return false;
        return stack.equals(itemStats.stack);
    }

    @Override
    public int hashCode() {
        int result = values.hashCode();
        result = 31 * result + stack.hashCode();
        return result;
    }

    public boolean shouldRender() {
        return values.size() > 0;
    }

    public ItemStack getStack() {
        return stack;
    }

    public Map<DisplayType<?>, StatEntry<?>> getValues() {
        return values;
    }

}

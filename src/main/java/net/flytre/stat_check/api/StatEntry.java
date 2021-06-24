package net.flytre.stat_check.api;

import net.flytre.flytre_lib.common.util.Formatter;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class StatEntry<T> {

    public static final String FORMAT_INT = "%d";
    public static final String FORMAT_FLOAT = "%.2f";


    protected final T value;

    protected StatEntry(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public abstract boolean isComparable();

    @SuppressWarnings("unchecked")
    public @Nullable CompareResult compare(Object equipped) {
        assert value.getClass().isAssignableFrom(equipped.getClass());
        return compareTo((T) equipped);
    }

    public abstract @Nullable CompareResult compareTo(T equipped);


    public abstract Text getRenderedText();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatEntry<?> statEntry = (StatEntry<?>) o;

        return Objects.equals(value, statEntry.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public enum CompareResult {
        GREATER,
        SAME,
        LOWER
    }

    public static class DoubleEntry extends StatEntry<Double> {

        public DoubleEntry(Double value) {
            super(value);
        }

        @Override
        public boolean isComparable() {
            return true;
        }

        @Override
        public @Nullable CompareResult compareTo(Double equipped) {
            return equipped > value ? CompareResult.LOWER : equipped < value ? CompareResult.GREATER : CompareResult.SAME;
        }


        @Override
        public Text getRenderedText() {
            return Text.of(format());
        }

        private String format() {
            if (value > 1000)
                return Formatter.formatNumber(value, "", false);
            else if (value == (int) (double) value)
                return String.format(FORMAT_INT, (int) (double) value);
            return String.format(FORMAT_FLOAT, value);
        }
    }

    public static class StringEntry extends StatEntry<String> {

        public StringEntry(String value) {
            super(value);
        }

        @Override
        public boolean isComparable() {
            return false;
        }

        @Override
        public @Nullable CompareResult compareTo(String equipped) {
            return null;
        }

        @Override
        public Text getRenderedText() {
            return Text.of(value);
        }
    }

    public static class TranslatableEntry extends StringEntry {

        public TranslatableEntry(String value) {
            super(value);
        }

        @Override
        public Text getRenderedText() {
            return new TranslatableText(value);
        }
    }
}

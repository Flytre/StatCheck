package net.flytre.stat_check.core;

import net.flytre.stat_check.api.DisplayType;
import net.flytre.stat_check.api.StatEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.Map;

public class DummyTooltipComponent implements TooltipComponent {
    private final int height;
    private final int width;


    public DummyTooltipComponent(ItemStats stats) {


        int secondWidth = 0;

        boolean second = false;
        int firstWidth = 0;
        for (var entry : stats.getValues().entrySet()) {
            if (!second)
                firstWidth = Math.max(firstWidth, simulateWidth(0, entry));
            else
                secondWidth = Math.max(secondWidth, simulateWidth(0, entry));
            second = !second;
        }

        float scale = 0.75f;
        this.width = (int) ((firstWidth + secondWidth) * scale);


        height = simulateHeight(stats);
    }

    public static int simulateHeight(ItemStats stats) {
        float scale = 0.75f;
        return (int) (Math.ceil(stats.getValues().size() / 2.0) * 18 * scale);
    }

    public static int simulateWidth(int x, Map.Entry<DisplayType<?>, StatEntry<?>> entry) {
        x += MinecraftClient.getInstance().textRenderer.getWidth(entry.getValue().getRenderedText());

        return x + 36;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return width;
    }
}

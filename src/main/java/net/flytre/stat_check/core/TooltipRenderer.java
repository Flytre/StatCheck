package net.flytre.stat_check.core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.flytre.stat_check.api.DisplayType;
import net.flytre.stat_check.api.StatEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TooltipRenderer {

    public final ItemStats data;

    public TooltipRenderer(ItemStack stack) {
        this.data = new ItemStats(stack);
    }


    public static ItemStats equipped(ItemStats data, MinecraftClient client) {
        assert client.player != null;
        ItemStack currentEquip;
        if (data.getStack().getItem() instanceof ArmorItem) {
            EquipmentSlot slot = ((ArmorItem) data.getStack().getItem()).getSlotType();
            currentEquip = client.player.getEquippedStack(slot);
        } else
            currentEquip = client.player.getMainHandStack();

        return currentEquip.isEmpty() ? null : new ItemStats(currentEquip);
    }

    public static Coord getCoord(int index) {
        int u = 16 * (index % 16);
        int v = 16 * (index / 16);
        return new Coord(u, v);
    }

    public static void drawIcon(MatrixStack matrices, int x, int y, int index) {
        Coord coord = getCoord(index);
        DrawableHelper.drawTexture(matrices, x, y, coord.x, coord.y, 16, 16, 256, 64);

    }

    public void render(MatrixStack matrices, int x, int y) {

        MinecraftClient client = MinecraftClient.getInstance();

        ItemRenderer itemRenderer = client.getItemRenderer();
        TextRenderer textRenderer = client.textRenderer;

        float f = itemRenderer.zOffset;
        itemRenderer.zOffset = 401.0F;

        x += 4;
        y -= DummyTooltipComponent.simulateHeight(data);
        y += 15;

        assert client.player != null;

        // Get currently equipped item
        ItemStats equippedStats = equipped(data, client);

        float scale = 0.75f;
        int x2 = (int) (x / scale);
        int y2 = (int) ((y - 16) / scale);

        matrices.push();
        matrices.scale(scale, scale, scale);


        boolean second = false;
        int secondColX = 0;
        int maxWidthFirst = 0;
        int maxWithSecond = 0;
        for (var entry : data.getValues().entrySet()) {
            if (!second) {
                secondColX = Math.max(secondColX, DummyTooltipComponent.simulateWidth(x2, entry));
                maxWidthFirst = Math.max(maxWidthFirst, textRenderer.getWidth(entry.getValue().getRenderedText()));
            } else {
                maxWithSecond = Math.max(maxWithSecond, textRenderer.getWidth(entry.getValue().getRenderedText()));
            }
            second = !second;
        }

        // Render stats
        second = false;
        for (var entry : data.getValues().entrySet()) {
            renderStat(matrices, textRenderer, second ? secondColX : x2, second ? maxWithSecond : maxWidthFirst, y2, entry, equippedStats);
            if (second) {
                y2 += 18;
            }
            second = !second;
        }

        matrices.pop();

        itemRenderer.zOffset = f;
    }

    private void renderStat(MatrixStack matrices, TextRenderer textRenderer, int x, int maxWidth, int y, Map.Entry<DisplayType<?>, StatEntry<?>> entry, @Nullable ItemStats equipped) {

        RenderSystem.setShaderTexture(0, entry.getKey().getTexture());

        drawIcon(matrices, x, y, entry.getKey().getIconIndex());
        x += 18;

        // Draw stat value
        RenderSystem.setShaderColor(0f, 0f, 0f, 1f);
        drawText(matrices, textRenderer, x, y + 5, entry.getValue().getRenderedText());
        x += maxWidth;

        RenderSystem.setShaderTexture(0, DisplayType.DEFAULT_TEXTURE);


        if (!data.equals(equipped)) {
            if (entry.getValue().isComparable() && equipped != null && (equipped.getValues().containsKey(entry.getKey()))) {
                StatEntry.CompareResult result = entry.getValue().compare(equipped.getValues().get(entry.getKey()).getValue());
                if (result == StatEntry.CompareResult.GREATER)
                    drawIcon(matrices, x, y, 14);
                else if (result == StatEntry.CompareResult.LOWER)
                    drawIcon(matrices, x, y, 15);
                else
                    drawIcon(matrices, x, y, 13);
            }
        }


    }

    public void drawText(MatrixStack matrices, TextRenderer textRenderer, int x, int y, Text text) {
        matrices.push();
        matrices.scale(1, 1, 1.33f);
        Matrix4f matrix4f = matrices.peek().getModel();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        matrices.translate(0.0D, 0.0D, 401.0D);
        textRenderer.draw(text, (float) x, y, -1, true, matrix4f, immediate, false, 0, 15728880);
        immediate.draw();
        matrices.pop();
    }

    record Coord(int x, int y) {
    }
}

package net.flytre.stat_check.mixin;

import net.flytre.stat_check.DummyTooltipComponent;
import net.flytre.stat_check.ItemStats;
import net.flytre.stat_check.TooltipRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;


@Mixin(Screen.class)
public class ScreenMixin {

    @Unique
    protected ItemStack currentRenderedStack = ItemStack.EMPTY;

    @Unique
    protected int startX;

    @Unique
    protected int endY;


    @Inject(method = "renderTooltipFromComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void stat_check$storeLocals(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci, int i, int j, int l, int m) {
        startX = l - 3;
        endY = m + j + 3;
    }


    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
    public void stat_check$setRenderedStack(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        currentRenderedStack = stack;
    }


    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("TAIL"))
    public void stat_check$renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        new TooltipRenderer(stack).render(matrices, startX, endY);
    }

    @Inject(method = "renderTooltipFromComponents", at = @At("TAIL"))
    public void stat_check$resetRenderedStack(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci) {
        currentRenderedStack = ItemStack.EMPTY;
    }

    @Inject(method = "renderTooltipFromComponents", at = @At(value = "HEAD"))
    public void stat_check$addExtraDims(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci) {
        if (components.size() > 0 && !currentRenderedStack.isEmpty()) {
            ItemStats stats = new ItemStats(currentRenderedStack);
            if (stats.shouldRender())
                components.add(new DummyTooltipComponent(stats));
        }
    }
}

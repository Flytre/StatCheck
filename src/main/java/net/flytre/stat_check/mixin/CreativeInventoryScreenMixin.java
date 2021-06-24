package net.flytre.stat_check.mixin;


import net.flytre.stat_check.StatCheck;
import net.flytre.stat_check.config.Config;
import net.flytre.stat_check.core.TooltipRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin extends ScreenMixin {


    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
    public void stat_check$setRenderedStack(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        currentRenderedStack = stack;
    }


    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("TAIL"))
    public void stat_check$renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        Config config = StatCheck.CONFIG.getConfig();
        if (!config.displayOnShiftOnly || Screen.hasShiftDown())
        new TooltipRenderer(stack).render(matrices, startX, endY);
    }

}

package net.flytre.stat_check.mixin;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.flytre.stat_check.StatCheck;
import net.flytre.stat_check.api.DisplayType;
import net.flytre.stat_check.config.Config;
import net.flytre.stat_check.core.ItemStats;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot);

    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
    public Multimap<EntityAttribute, EntityAttributeModifier> stat_check$attributeTooltip(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> multimap = this.getAttributeModifiers(slot);
        Config config = StatCheck.CONFIG.getConfig();

        if(config.displayOnShiftOnly && (!Screen.hasShiftDown()))
            return multimap;

        Multimap<EntityAttribute, EntityAttributeModifier> tweaked = HashMultimap.create();


        ItemStats stats = new ItemStats(stack);
        Set<EntityAttribute> covered = stats.getValues().keySet().stream().map(DisplayType::getAttribute).collect(Collectors.toSet());;


        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : multimap.entries()) {
            if (!covered.contains(entry.getKey()))
                tweaked.put(entry.getKey(), entry.getValue());
        }

//        for (EntityAttribute attribute : multimap.keys())
//            if (!covered.contains(attribute))
//                tweaked.replaceValues(attribute, multimap.get(attribute));

        return tweaked;
    }
}

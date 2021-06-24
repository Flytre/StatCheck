package net.flytre.stat_check.mixin;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.flytre.stat_check.api.DisplayType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot);

    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
    public Multimap<EntityAttribute, EntityAttributeModifier> stat_check$attributeTooltip(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> multimap = this.getAttributeModifiers(slot);
        Multimap<EntityAttribute, EntityAttributeModifier> tweaked = HashMultimap.create();

        for (EntityAttribute attribute : multimap.keys())
            if (!DisplayType.getCoveredAttributes().contains(attribute))
                tweaked.replaceValues(attribute, multimap.get(attribute));

        return tweaked;
    }
}

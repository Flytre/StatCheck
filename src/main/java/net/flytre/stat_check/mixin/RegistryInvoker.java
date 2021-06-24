package net.flytre.stat_check.mixin;


import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(Registry.class)
public interface RegistryInvoker {


    @Invoker("create")
     static <T> Registry<T> createRegistry(RegistryKey<? extends Registry<T>> key, Supplier<T> defaultEntry) {
        throw new AssertionError("Invoked Method was called");

    }
}

package net.flytre.stat_check;

import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.flytre.flytre_lib.config.ConfigHandler;
import net.flytre.flytre_lib.config.ConfigRegistry;
import net.flytre.flytre_lib.config.GsonHelper;
import net.flytre.stat_check.api.DisplayType;
import net.flytre.stat_check.api.DisplayTypeRegistry;
import net.flytre.stat_check.config.Config;

import java.util.Map;

public class StatCheck implements ClientModInitializer {


    public static final Gson GSON = GsonHelper
            .GSON_BUILDER
            .registerTypeAdapter(DisplayType.class, new GsonHelper.IdentifierBasedSerializer<DisplayType<?>>(DisplayTypeRegistry.DISPLAY_TYPE_REGISTRY::get, DisplayTypeRegistry.DISPLAY_TYPE_REGISTRY::getId))
            .registerTypeAdapter(Map.class, new GsonHelper.MapDeserializer<DisplayType<?>,Boolean>(DisplayTypeRegistry.DISPLAY_TYPE_REGISTRY::get))
            .enableComplexMapKeySerialization()
            .create();


    public static ConfigHandler<Config> CONFIG = new ConfigHandler<>(new Config(), "stat_check", GSON);


    @Override
    public void onInitializeClient() {
        ConfigRegistry.registerClientConfig(CONFIG);
    }
}

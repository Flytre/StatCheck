package net.flytre.stat_check.config;

import com.google.gson.annotations.SerializedName;
import net.flytre.flytre_lib.config.ConfigEventAcceptor;
import net.flytre.stat_check.StatCheck;
import net.flytre.stat_check.api.DisplayType;
import net.flytre.stat_check.api.DisplayTypeRegistry;

import java.util.HashMap;
import java.util.Map;

public class Config implements ConfigEventAcceptor {


    @SerializedName("rendered_stats")
    public Map<DisplayType<?>, Boolean> renderedStats = new HashMap<>();

    @SerializedName("display_on_shift_only")
    public boolean displayOnShiftOnly = false;

    @Override
    public void onReload() {
        boolean modified = false;
        for (var type : DisplayTypeRegistry.values()) {
            if (!(renderedStats.containsKey(type))) {
                renderedStats.put(type, modified = true);
            }
        }
        if (modified)
            StatCheck.CONFIG.save(this);
    }
}

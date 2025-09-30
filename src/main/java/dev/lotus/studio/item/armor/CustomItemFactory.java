package dev.lotus.studio.item.armor;

import dev.lotus.studio.item.Provider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class CustomItemFactory {
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final boolean oraxenEnabled = pluginManager.isPluginEnabled("Oraxen");
    private final boolean nexoEnabled   = pluginManager.isPluginEnabled("Nexo");

    public CustomItem fromSection(ConfigurationSection section) {
        Objects.requireNonNull(section, "section");

        String providerStr = section.getString("provider", section.getString("type", "standard"));
        Provider provider = Provider.valueOf(providerStr.toUpperCase(Locale.ROOT));

        String displayName = section.getString("displayName", null);
        List<String> lore   = section.getStringList("lore");
        double tempRes = section.getDouble("temperatureResistance", 0.0);
        double radRes  = section.getDouble("radiationResistance", 0.0);

        switch (provider) {
            case STANDARD: {
                String matName = section.getString("material");
                if (matName == null) throw new IllegalArgumentException("Отсутствует material");
                Material mat = Material.valueOf(matName.toUpperCase(Locale.ROOT));
                return new StandardArmor(mat, displayName, lore, tempRes, radRes);
            }
            case ORAXEN: {
                if (!oraxenEnabled) throw new IllegalStateException("Oraxen недоступен/не загружен");
                String id = section.getString("oraxenId");
                if (id == null || id.isEmpty())
                    throw new IllegalArgumentException("Отсутствует oraxenId");
                return new OraxenCustomItem(id, tempRes, radRes, displayName, lore);
            }
            case NEXO: {
                if (!nexoEnabled) throw new IllegalStateException("Nexo недоступен/не загружен");
                String id = section.getString("nexoId");
                if (id == null || id.isEmpty())
                    throw new IllegalArgumentException("Отсутствует nexoId");
                return new NexoCustomItem(id, tempRes, radRes, displayName, lore);
            }
            default:
                throw new IllegalArgumentException("Неизвестный provider: " + provider);
        }
    }
}

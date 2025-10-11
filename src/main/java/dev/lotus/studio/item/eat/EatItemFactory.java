package dev.lotus.studio.item.eat;

import dev.lotus.studio.item.Provider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class EatItemFactory {
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    public EatItem fromSection(ConfigurationSection section) {
        Objects.requireNonNull(section, "section");

        String providerStr = section.getString("provider", section.getString("type", "standard"));
        Provider provider = Provider.valueOf(providerStr.toUpperCase(Locale.ROOT));

        int radiation = section.getInt("foodValue.radiation");
        int temperature = section.getInt("foodValue.temperature");

        switch (provider) {
            case STANDARD: {
                String matName = section.getString("material");
                if (matName == null) throw new IllegalArgumentException("Отсутствует material");
                Material mat = Material.valueOf(matName);

                String displayName = section.getString("displayName", null);
                List<String> lore = section.getStringList("lore");

                return new StandardEatItem(mat, displayName, lore, radiation, temperature);
            }
            case ORAXEN: {
                requireEnabled("Oraxen");
                String id = section.getString("oraxenId");
                if (id == null || id.isEmpty())
                    throw new IllegalArgumentException("Отсутствует oraxenId");
                return new OraxenEatItem(id, radiation, temperature,
                        section.getString("displayName", null),
                        section.getStringList("lore"));
            }
            case NEXO: {
                requireEnabled("Nexo");
                String id = section.getString("id", section.getString("nexoId"));
                if (id == null || id.isEmpty())
                    throw new IllegalArgumentException("Отсутствует Nexo id");
                return new NexoEatItem(id, radiation, temperature,
                        section.getString("displayName", null),
                        section.getStringList("lore"));
            }
            default:
                throw new IllegalArgumentException("Неизвестный provider: " + provider);
        }
    }

    private void requireEnabled(String pluginName) {
        if (!pluginManager.isPluginEnabled(pluginName)) {
            throw new IllegalStateException(pluginName + " недоступен/не загружен");
        }
    }
}

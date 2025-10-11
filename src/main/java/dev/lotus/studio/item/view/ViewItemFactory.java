package dev.lotus.studio.item.view;

import dev.lotus.studio.item.Provider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ViewItemFactory {
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    public ViewItem fromSection(ConfigurationSection section) {
        Objects.requireNonNull(section, "section");

        // Поддерживаем и "provider", и старое "type"
        String providerStr = section.getString("provider", section.getString("type", "standard"));
        Provider provider = Provider.valueOf(providerStr.toUpperCase(Locale.ROOT));
        String viewType = section.getString("view_type", "default");
        String displayName = section.getString("displayName", null);
        List<String> lore = section.getStringList("lore");

        switch (provider) {
            case STANDARD: {
                String matName = section.getString("material");
                if (matName == null) throw new IllegalArgumentException("Отсутствует material");
                Material mat = Material.valueOf(matName);
                return new StandardViewItem(mat, displayName, lore, viewType);
            }
            case ORAXEN: {
                requireEnabled("Oraxen");
                String id = section.getString("oraxenId");
                if (id == null || id.isEmpty())
                    throw new IllegalArgumentException("Отсутствует oraxenId");
                return new OraxenViewItem(id, displayName, lore, viewType);
            }
            case NEXO: {
                requireEnabled("Nexo");
                String id = section.getString("id", section.getString("nexoId"));
                if (id == null || id.isEmpty())
                    throw new IllegalArgumentException("Отсутствует Nexo id");
                return new NexoViewItem(id, displayName, lore, viewType);
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

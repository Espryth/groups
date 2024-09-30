package me.espryth.groupsplugin.lang.translator;

import java.util.HashMap;
import me.espryth.groupsplugin.config.BukkitConfigFile;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides language resources for the plugin.
 */
public final class LangResourceProvider {

  private static final String LANG_PREFIX = "lang";
  public static final String DEFAULT_LOCALE = "en";

  private static ConfigFileTranslator translator;

  /**
   * Load the language files.
   *
   * @param plugin The plugin instance.
   */
  @SuppressWarnings("all")
  public static void load(final @NotNull Plugin plugin) {
    if (translator != null) {
      throw new IllegalStateException("Language files already loaded!");
    }
    final var folder = plugin.getDataFolder();
    final var files = new HashMap<String, BukkitConfigFile>();
    if (folder.exists()) {
      final var list = folder.listFiles();
      if (list != null) {
        for (final var file : list) {
          final var name = file.getName();
          if (name.startsWith(LANG_PREFIX) && name.endsWith(".yml")) {
            final var lang = name.substring(LANG_PREFIX.length() + 1, name.length() - 4);
            files.put(lang, BukkitConfigFile.create(plugin, name));
          }
        }
      }
    }
    if (!files.containsKey(DEFAULT_LOCALE)) {
      files.put(DEFAULT_LOCALE, BukkitConfigFile.create(plugin, "lang_en.yml"));
    }
    translator = new ConfigFileTranslator(Key.key(plugin.getName().toLowerCase()), files);
    GlobalTranslator.translator().addSource(translator);
  }

  /**
   * Get the translator.
   *
   * @return The translator instance.
   */
  public static ConfigFileTranslator getTranslator() {
    return translator;
  }
}

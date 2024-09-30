package me.espryth.groupsplugin.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link YamlConfiguration} attached to a file, which may or
 * may not reflect a default (in-jar) configuration. (resources)
 *
 * @author yusshu (Andre Roldan)
 */
public final class BukkitConfigFile extends YamlConfiguration {

  private final Path path;
  private final @Nullable Supplier<InputStream> resource;

  private BukkitConfigFile(final @NotNull Path path, final @Nullable Supplier<InputStream> resource) {
    this.path = path;
    this.resource = resource;
  }

  /**
   * Gets the given path as a {@link Component}.
   *
   * @param path The path to get.
   * @return The component or null if not found.
   */
  @Nullable
  public Component getComponent(@NotNull final String path) {
    return getComponent(path, TagResolver.empty());
  }

  /**
   * Gets the given path as a {@link Component} with the given resolvers.
   *
   * @param path The path to get.
   * @param resolver The resolvers to use.
   * @return The component or null if not found.
   */
  @Nullable
  public Component getComponent(@NotNull final String path, @NotNull final TagResolver... resolver) {
    final var object = this.get(path);
    if (object == null) {
      return null;
    }
    if (object instanceof List<?> list) {
      final var components = new ArrayList<Component>(list.size());
      for (final var element : list) {
        final var component = MiniMessage.miniMessage()
            .deserialize(element.toString(), resolver)
            .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        components.add(component);
      }
      return Component.join(JoinConfiguration.newlines(), components);
    }
    return MiniMessage.miniMessage()
      .deserialize(object.toString(), resolver)
      .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
  }

  /**
   * Loads/reloads the configuration from the file to memory.
   *
   * <p>If the file doesn't exist AND this configuration reflects a default
   * one (from resources), the default one will copied and loaded.</p>
   *
   * <p>All the values contained within this configuration will be removed,
   * leaving only settings and defaults, and the new values will be loaded
   * from the given string.</p>
   *
   * @throws IllegalStateException If configuration is invalid or an I/O
   *                               exception occurs.
   */
  public void load() {
    if (!Files.exists(path) && resource != null) {
      createParentDirs();
      try (final InputStream input = resource.get()) {
        Files.copy(input, path);
      } catch (final IOException e) {
        throw new IllegalStateException("Failed to copy default configuration from resource to " + path, e);
      }
    }
    // updates the configuration with the file contents
    try (final var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      load(reader);
    } catch (final InvalidConfigurationException e) {
      throw new IllegalStateException("Failed to read invalid YAML configuration from " + path, e);
    } catch (final IOException e) {
      throw new IllegalStateException("Failed to read YAML from " + path, e);
    }
  }

  /**
   * Saves the configuration from memory to the file.
   *
   * <p>Note that this method will try to create parent folders
   * if they don't exist.</p>
   *
   * @throws IllegalStateException If couldn't create file or
   *                               an I/O exception occurs.
   */
  public void save() {
    // create path parent dirs
    createParentDirs();
    // serialize to string
    final var data = saveToString();
    // write string
    try (final Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      writer.write(data);
    } catch (final IOException e) {
      throw new IllegalStateException("Failed to write YAML to " + path, e);
    }
  }

  private void createParentDirs() {
    try {
      final var parent = path.getParent();
      if (parent != null) {
        Files.createDirectories(path.getParent());
        if (!Files.isDirectory(parent)) {
          throw new IllegalStateException("Unable to create parent directories for " + path);
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to create parent directories for " + path, e);
    }
  }

  /**
   * Creates a new {@link BukkitConfigFile} attached to the
   * given path, note that this method <b>will not load</b> the
   * configuration from the file.
   *
   * @param path the path
   * @return the new configuration
   * @throws NullPointerException If path is null
   */
  public static @NotNull BukkitConfigFile create(final @NotNull Path path) {
    return new BukkitConfigFile(path, null);
  }

  /**
   * Creates a new {@link BukkitConfigFile} representing the given
   * configuration. Note that this method <b>will perform an initial
   * configuration load</b> from the file (or from the resource if the
   * file doesn't exist).
   *
   * <p>The file will be created if it doesn't exist.</p>
   *
   * <p>The file will be at the plugin data folder, with the given name.</p>
   *
   * <p>The default configuration must be a plugin resource, with the same name.</p>
   *
   * @param plugin                The owner plugin
   * @param configurationFileName The configuration file name
   * @return The new configuration
   */
  public static @NotNull BukkitConfigFile create(final @NotNull Plugin plugin, final @NotNull String configurationFileName) {
    final var path = plugin.getDataFolder().toPath().resolve(configurationFileName);
    final var config = new BukkitConfigFile(path, () -> plugin.getResource(configurationFileName));
    config.load(); // first load
    return config;
  }
}

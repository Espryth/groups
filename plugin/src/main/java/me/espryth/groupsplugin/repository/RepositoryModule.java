package me.espryth.groupsplugin.repository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import me.espryth.groupsplugin.repository.connection.Connection;
import me.espryth.groupsplugin.repository.connection.ConnectionConfig;
import org.bukkit.plugin.Plugin;
import org.jdbi.v3.core.Jdbi;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * Module for database connection.
 */
public final class RepositoryModule extends AbstractModule {

  @Provides
  @Singleton
  ConnectionConfig provideConnectionConfig(final Plugin plugin) {
    try {
      final var loader = YamlConfigurationLoader.builder()
          .nodeStyle(NodeStyle.BLOCK)
          .path(plugin.getDataPath().resolve("connection.yml"))
          .build();
      final var node = loader.load();
      final var config = node.get(ConnectionConfig.class);
      node.set(ConnectionConfig.class, config);
      loader.save(node);
      return config;
    } catch (ConfigurateException e) {
      throw new RuntimeException(e);
    }
  }

  @Provides
  @Singleton
  Executor provideAsyncExecutor() {
    return Executors.newFixedThreadPool(8);
  }

  @Provides
  @Singleton
  Jdbi provideConnection(final Plugin plugin, final ConnectionConfig config) {
    final var connection = new Connection(plugin, config);
    connection.connect();
    return connection.get();
  }
}

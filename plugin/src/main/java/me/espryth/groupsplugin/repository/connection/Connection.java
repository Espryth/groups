package me.espryth.groupsplugin.repository.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Represents a connection with the database.
 */
public final class Connection {

  private final Plugin plugin;
  private final Logger logger;
  private final ConnectionConfig config;

  private Jdbi jdbi;

  /**
   * Creates a new connection with the database.
   *
   * @param plugin The plugin instance.
   * @param config The connection configuration.
   */
  public Connection(
      final @NotNull Plugin plugin,
      final @NotNull ConnectionConfig config
  ) {
    this.plugin = plugin;
    this.logger = plugin.getSLF4JLogger();
    this.config = config;
  }

  /**
   * Starts the connection with the database.
   */
  public void connect() {
    final var driver = this.config.driver();
    final var credentials = this.config.credentials();
    final var hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(driver.clazz());
    hikariConfig.setMaximumPoolSize(6);
    switch (config.driver()) {
      case MySQL -> {
        hikariConfig.setJdbcUrl(String.format(driver.url(), credentials.host(), credentials.port(), credentials.database()));
        hikariConfig.setUsername(credentials.username());
        hikariConfig.setPassword(credentials.password());
      }
      case SQLite -> hikariConfig.setJdbcUrl(String.format(driver.url(), this.plugin.getDataFolder().getAbsolutePath(), credentials.database()));
      default -> throw new IllegalStateException("Unexpected value: " + config.driver());
    }
    final var dataSource = new HikariDataSource(hikariConfig);
    this.jdbi = Jdbi.create(dataSource);
    jdbi.useHandle(handle -> handle.execute("PRAGMA foreign_keys = ON;"));
    this.logger.info("Connection established with the database successfully. Driver: {} ", driver.name());
  }

  /**
   * Gets the Jdbi instance.
   *
   * @return The Jdbi instance.
   */
  public @NotNull Jdbi get() {
    if (this.jdbi == null) {
      throw new IllegalStateException("The connection has not been established yet.");
    }
    return this.jdbi;
  }
}

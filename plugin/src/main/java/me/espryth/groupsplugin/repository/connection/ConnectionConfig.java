package me.espryth.groupsplugin.repository.connection;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * Represents the connection configuration.
 */
@ConfigSerializable
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConnectionConfig {

  private Driver driver = Driver.SQLite;
  private Credentials credentials = new Credentials("database", "localhost", 3306, "root", "root");

  /**
   * Returns the driver.
   *
   * @return The driver.
   */
  public Driver driver() {
    return driver;
  }

  /**
   * Returns the credentials.
   *
   * @return The credentials.
   */
  public Credentials credentials() {
    return credentials;
  }

  /**
   * Represents the database credentials.
   *
   * @param database The database name.
   * @param host The host.
   * @param port The port.
   * @param username The username.
   * @param password The password.
   */
  @ConfigSerializable
  public record Credentials(String database, String host, int port, String username, String password) {
  }

}

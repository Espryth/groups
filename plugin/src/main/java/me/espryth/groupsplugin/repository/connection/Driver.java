package me.espryth.groupsplugin.repository.connection;

import java.io.File;
import org.jetbrains.annotations.NotNull;

/**
 * Enum that represents the SQL drivers.
 */
public enum Driver {

  SQLite(
      "jdbc:sqlite:%s" + File.separator + "%s.db",
      "org.sqlite.JDBC"
  ),
  MySQL(
      "jdbc:mysql://%s:%s/%s",
      "com.mysql.cj.jdbc.Driver"
  );

  private final String url;
  private final String clazz;

  Driver(String url, String clazz) {
    this.url = url;
    this.clazz = clazz;
  }

  /**
   * Get the url of the driver.
   *
   * @return the url of the driver.
   */
  public @NotNull String url() {
    return url;
  }

  /**
   * Get the class of the driver.
   *
   * @return the class of the driver.
   */
  public @NotNull String clazz() {
    return clazz;
  }
}

package me.espryth.groupsplugin.repository.mapping.column;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data type in a column.
 */
public enum DataType {

  TIMESTAMP("TIMESTAMP"),
  BOOLEAN("TINYINT(1)"),
  STRING("VARCHAR(255)"),
  NUMBER("INT"),
  LONG("BIGINT"),
  DECIMAL("FLOAT"),
  UUID("VARCHAR(36)");

  private static final Map<Class<?>, DataType> CLASS_TO_DATA_TYPE = Map.ofEntries(
      Map.entry(String.class, STRING),
      Map.entry(Integer.class, NUMBER),
      Map.entry(int.class, NUMBER),
      Map.entry(Long.class, LONG),
      Map.entry(long.class, LONG),
      Map.entry(Boolean.class, BOOLEAN),
      Map.entry(boolean.class, BOOLEAN),
      Map.entry(Float.class, DECIMAL),
      Map.entry(float.class, DECIMAL),
      Map.entry(Double.class, DECIMAL),
      Map.entry(double.class, DECIMAL),
      Map.entry(UUID.class, UUID),
      Map.entry(Instant.class, TIMESTAMP)
  );

  private final String sql;

  DataType(final @NotNull String sql) {
    this.sql = sql;
  }

  /**
   * Returns the SQL representation of the data type.
   *
   * @return The SQL representation of the data type.
   */
  public @NotNull String toSql() {
    return sql;
  }

  /**
   * Returns the data type from a class.
   *
   * @param type The class to get the data type from.
   * @return The data type from the class.
   */
  public static @NotNull DataType fromClass(final @NotNull Class<?> type) {
    return Objects.requireNonNull(CLASS_TO_DATA_TYPE.get(type), "The class " + type.getName() + " doesn't have a DataType.");
  }
}

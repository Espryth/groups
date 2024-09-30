package me.espryth.groupsplugin.repository.mapping.column.foreign;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a trigger in a foreign key.
 */
public enum ForeignTrigger {
  DELETE("ON DELETE"),
  UPDATE("ON UPDATE");

  private final String sql;

  ForeignTrigger(final @NotNull String sql) {
    this.sql = sql;
  }

  public @NotNull String toSql() {
    return sql;
  }
}

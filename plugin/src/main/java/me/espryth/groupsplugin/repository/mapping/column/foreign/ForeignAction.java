package me.espryth.groupsplugin.repository.mapping.column.foreign;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an action in a foreign key.
 */
public enum ForeignAction {

  RESTRICT("RESTRICT"),
  CASCADE("CASCADE"),
  NULL("SET NULL"),
  NOTHING("NO ACTION"),
  DEFAULT("SET DEFAULT");

  private final String sql;

  ForeignAction(final @NotNull String sql) {
    this.sql = sql;
  }

  /**
   * Returns the SQL representation of this action.
   *
   * @return The SQL representation of this action.
   */
  public @NotNull String toSql() {
    return sql;
  }
}

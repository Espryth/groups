package me.espryth.groupsplugin.repository.mapping.column;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a constraint in a column.
 */
public enum Constraint {

  NOT_NULL("NOT NULL"),
  UNIQUE("UNIQUE"),
  PRIMARY("PRIMARY KEY"),
  SECONDARY("");

  private final String sql;

  Constraint(final @NotNull String sql) {
    this.sql = sql;
  }

  /**
   * Returns the SQL representation of the constraint.
   *
   * @return the SQL representation of the constraint.
   */
  public @NotNull String toSql() {
    return sql;
  }
}

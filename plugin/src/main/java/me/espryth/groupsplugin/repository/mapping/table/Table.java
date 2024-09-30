package me.espryth.groupsplugin.repository.mapping.table;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.espryth.groupsplugin.repository.mapping.column.Column;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a table in the database.
 */
public final class Table {

  private final String name;
  private final List<Column> columns;
  private String columnNames;
  private String valueNames;

  public Table(@NotNull String name, @NotNull List<Column> columns) {
    this.name = name;
    this.columns = columns;
  }

  /**
   * Returns the name of the table.
   *
   * @return The name of the table.
   */
  public @NotNull String name() {
    return name;
  }

  /**
   * Returns the columns of the table.
   *
   * @return The columns of the table.
   */
  public @NotNull List<Column> columns() {
    return columns;
  }

  /**
   * Converts the table to a SQL string.
   *
   * @return The SQL string.
   */
  public @NotNull String toSql() {
    final var foreignKeys = this.columns.stream()
        .map(Column::foreignKey)
        .filter(Objects::nonNull)
        .toList();
    return this.columns.stream()
        .map(Column::toSql)
        .collect(Collectors.joining(", ")) + (foreignKeys.isEmpty()
        ? ""
        : ", " + foreignKeys.stream().map(ForeignKey::toSql).collect(Collectors.joining(", ")));
  }

  /**
   * Returns the column names separated by commas.
   *
   * @return The column names.
   */
  public @NotNull String columnNames() {
    if (this.columnNames == null) {
      this.columnNames = this.columns.stream()
        .map(Column::name)
        .collect(Collectors.joining(","));
    }
    return columnNames;
  }

  /**
   * Returns the value names separated by commas.
   *
   * @return The value names.
   */
  public @NotNull String valueNames() {
    if (this.valueNames == null) {
      this.valueNames = this.columns.stream()
        .map(column -> ":" + column.name())
        .collect(Collectors.joining(","));
    }
    return valueNames;
  }
}

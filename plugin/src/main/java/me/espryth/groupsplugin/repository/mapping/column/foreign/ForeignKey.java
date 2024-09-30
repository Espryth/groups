package me.espryth.groupsplugin.repository.mapping.column.foreign;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a foreign key in a column.
 *
 * @param table The table that the column references.
 * @param column The column that references the table.
 * @param actions The actions that the foreign key will do.
 */
public record ForeignKey(@NotNull String field, @NotNull String table, @NotNull String column, @NotNull Map<ForeignTrigger, ForeignAction> actions) {

  /**
   * Creates a new builder for a foreign key.
   *
   * @return A new builder for a foreign key.
   */
  public static @NotNull Builder builder() {
    return new Builder();
  }

  /**
   * Converts the foreign key to a SQL string.
   *
   * @return The foreign key as a SQL string.
   */
  public @NotNull String toSql() {
    return "FOREIGN KEY (" + field + ") REFERENCES " + table + "(" + column + ") " + actions.entrySet().stream()
      .map(entry -> entry.getKey().toSql() + " " + entry.getValue().toSql())
      .reduce((a, b) -> a + " " + b)
      .orElse("");
  }

  /**
   * A builder for a foreign key.
   */
  public static final class Builder {

    private String field;
    private String table;
    private String column;
    private final Map<ForeignTrigger, ForeignAction> actions = new HashMap<>();

    private Builder() {
    }

    public @NotNull Builder field(final @NotNull String field) {
      this.field = field;
      return this;
    }

    public @NotNull Builder table(final @NotNull String table) {
      this.table = table;
      return this;
    }

    public @NotNull Builder column(final @NotNull String column) {
      this.column = column;
      return this;
    }

    public @NotNull Builder action(final @NotNull ForeignTrigger trigger, final @NotNull ForeignAction action) {
      this.actions.put(trigger, action);
      return this;
    }

    /**
     * Builds the foreign key.
     *
     * @return The foreign key.
     */
    public @NotNull ForeignKey build() {
      return new ForeignKey(
          Objects.requireNonNull(field),
          Objects.requireNonNull(table),
          Objects.requireNonNull(column),
          actions
      );
    }
  }
}

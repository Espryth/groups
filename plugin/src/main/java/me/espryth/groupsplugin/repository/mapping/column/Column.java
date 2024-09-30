package me.espryth.groupsplugin.repository.mapping.column;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a column in a table.
 *
 * @param name The name of the column.
 * @param type The data type of the column.
 * @param constraints The constraints of the column.
 * @param foreignKey The foreign key of the column.
 */
public record Column(@NotNull String name, @NotNull DataType type, @NotNull Set<Constraint> constraints, @Nullable ForeignKey foreignKey) {

  /**
   * Creates a new builder for a column.
   *
   * @return The new builder.
   */
  public static @NotNull Builder builder() {
    return new Builder();
  }

  /**
   * Converts the column to a SQL representation.
   *
   * @return The SQL representation of the column.
   */
  public @NotNull String toSql() {
    return this.name + " " + type.toSql()
        + constraints.stream()
        .map(Constraint::toSql)
        .collect(Collectors.joining(" "));
  }

  /**
   * Represents a builder for a column.
   */
  public static final class Builder {

    private String name;
    private DataType dataType;
    private ForeignKey foreignKey;
    private final Set<Constraint> constraints = new HashSet<>();

    private Builder() {
    }

    public @NotNull Builder name(final @NotNull String name) {
      this.name = name;
      return this;
    }

    public @NotNull Builder type(final @NotNull DataType dataType) {
      this.dataType = dataType;
      return this;
    }

    public @NotNull Builder constraint(final @NotNull Constraint constraint) {
      this.constraints.add(constraint);
      return this;
    }

    public @NotNull Builder foreignKey(final @NotNull ForeignKey foreignKey) {
      this.foreignKey = foreignKey;
      return this;
    }

    public @NotNull Column build() {
      return new Column(name, dataType, constraints, foreignKey);
    }
  }

}

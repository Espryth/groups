package me.espryth.groupsplugin.repository.mapping.table;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import me.espryth.groupsplugin.repository.mapping.annotation.Constraints;
import me.espryth.groupsplugin.repository.mapping.annotation.Entity;
import me.espryth.groupsplugin.repository.mapping.annotation.ForeignKey;
import me.espryth.groupsplugin.repository.mapping.column.Column;
import me.espryth.groupsplugin.repository.mapping.column.DataType;
import org.jetbrains.annotations.NotNull;

/**
 * A factory to create tables.
 */
public final class TableFactory {

  /**
   * Create a table from a class.
   *
   * @param type The class to create the table.
   * @return The table created.
   */
  public static @NotNull Table create(final @NotNull Class<?> type) {
    final var entity = type.getAnnotation(Entity.class);
    if (entity == null) {
      throw new IllegalArgumentException("The class " + type.getName() + " doesn't have a Entity annotation.");
    }
    final var columns = new LinkedList<Column>();
    for (final var field : type.getDeclaredFields()) {
      if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
        continue;
      }
      final var columnBuilder = Column.builder()
          .name(field.getName())
          .type(DataType.fromClass(field.getType()));
      final var constraints = field.getAnnotation(Constraints.class);
      final var foreignKey = field.getAnnotation(ForeignKey.class);
      if (constraints != null) {
        for (final var constraint : constraints.value()) {
          columnBuilder.constraint(constraint);
        }
      }
      if (foreignKey != null) {
        final var foreignKeyBuilder = me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignKey.builder()
            .field(field.getName())
            .table(foreignKey.table())
            .column(foreignKey.column());
        for (final var action : foreignKey.actions()) {
          foreignKeyBuilder.action(action.trigger(), action.action());
        }
        columnBuilder.foreignKey(foreignKeyBuilder.build());
      }
      columns.add(columnBuilder.build());
    }
    return new Table(entity.table(), columns);
  }
}

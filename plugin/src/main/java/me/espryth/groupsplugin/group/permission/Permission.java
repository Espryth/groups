package me.espryth.groupsplugin.group.permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.repository.mapping.annotation.Constraints;
import me.espryth.groupsplugin.repository.mapping.annotation.Entity;
import me.espryth.groupsplugin.repository.mapping.annotation.ForeignKey;
import me.espryth.groupsplugin.repository.mapping.annotation.ForeignKeyAction;
import me.espryth.groupsplugin.repository.mapping.codec.Codec;
import me.espryth.groupsplugin.repository.mapping.column.Constraint;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignAction;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignTrigger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a permission of a group.
 */
@Entity(table = "group_permissions")
public final class Permission {

  public static final Codec<Permission> CODEC = new PermissionCodec();

  @Constraints(Constraint.PRIMARY)
  private final UUID id;

  @ForeignKey(
      table = "groups",
      column = "id",
      actions = {
          @ForeignKeyAction(
              trigger = ForeignTrigger.DELETE,
              action = ForeignAction.CASCADE
          )
      }
  )
  private final UUID groupId;

  private final String name;

  private boolean value;

  private Permission(
      final @NotNull UUID id,
      final @NotNull UUID groupId,
      final @NotNull String name,
      final boolean value
  ) {
    this.id = id;
    this.groupId = groupId;
    this.name = name;
    this.value = value;
  }

  /**
   * Creates a new permission.
   *
   * @param group The group that the permission belongs to.
   * @param name The name of the permission.
   * @param value The value of the permission.
   * @return The created permission.
   */
  public static @NotNull Permission create(
      final @NotNull Group group,
      final @NotNull String name,
      final boolean value
  ) {
    return new Permission(UUID.randomUUID(), group.id(), name, value);
  }

  public @NotNull UUID id() {
    return this.id;
  }

  public @NotNull UUID groupId() {
    return this.groupId;
  }

  public @NotNull String name() {
    return this.name;
  }

  public boolean value() {
    return this.value;
  }

  public void value(final boolean value) {
    this.value = value;
  }

  /**
   * Creates a tag resolver for this permission.
   *
   * @return The created tag resolver.
   */
  public @NotNull TagResolver tagResolver() {
    return TagResolver.builder()
        .resolver(TagResolver.resolver("permission", Tag.selfClosingInserting(Component.text(this.name()))))
        .resolver(TagResolver.resolver("value", Tag.selfClosingInserting(Component.text(this.value()))))
        .build();
  }

  private static class PermissionCodec implements Codec<Permission> {

    @Override
    public @NotNull Map<String, Object> encode(final @NotNull Permission model) {
      return Map.of(
          "id", model.id().toString(),
          "groupId", model.groupId().toString(),
          "name", model.name(),
          "value", model.value()
      );
    }

    @Override
    public @NotNull Permission decode(final @NotNull ResultSet resultSet) throws SQLException {
      return new Permission(
          UUID.fromString(resultSet.getString("id")),
          UUID.fromString(resultSet.getString("groupId")),
          resultSet.getString("name"),
          resultSet.getBoolean("value")
      );
    }
  }
}

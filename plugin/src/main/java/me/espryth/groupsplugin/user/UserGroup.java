package me.espryth.groupsplugin.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

/**
 * Represents a user group relationship.
 */
@Entity(table = "user_groups")
public final class UserGroup {

  public static final Codec<UserGroup> CODEC = new UserGroupCodec();

  @Constraints(Constraint.PRIMARY)
  private final UUID id;

  private final UUID userId;

  @ForeignKey(
      table = "groups",
      column = "id",
      actions = {
          @ForeignKeyAction(
              trigger = ForeignTrigger.DELETE,
              action = ForeignAction.CASCADE
          ),
      }
  )
  private final UUID groupId;

  private final long createdAt;

  private final long duration;

  private UserGroup(
      final @NotNull UUID id,
      final @NotNull UUID userId,
      final @NotNull UUID groupId,
      final long createdAt,
      final long duration
  ) {
    this.id = id;
    this.userId = userId;
    this.groupId = groupId;
    this.createdAt = createdAt;
    this.duration = duration;
  }

  /**
   * Creates a new user group relationship.
   *
   * @param user The user.
   * @param group The group.
   * @param duration The duration of the relationship.
   */
  public UserGroup(
      final @NotNull User user,
      final @NotNull Group group,
      final @NotNull Duration duration
  ) {
    this.id = UUID.randomUUID();
    this.userId = user.id();
    this.groupId = group.id();
    this.createdAt = Instant.now().toEpochMilli();
    this.duration = duration.toMillis();
  }

  public @NotNull UUID id() {
    return id;
  }

  public @NotNull UUID userId() {
    return userId;
  }

  public @NotNull UUID groupId() {
    return groupId;
  }

  public long createdAt() {
    return createdAt;
  }

  public long duration() {
    return duration;
  }

  public boolean isExpired() {
    return duration != 0 && this.createdAt + this.duration < Instant.now().toEpochMilli();
  }

  @TestOnly
  boolean isExpired(final Instant now) {
    return duration != 0 && this.createdAt + this.duration < now.toEpochMilli();
  }

  private static class UserGroupCodec implements Codec<UserGroup> {

    @Override
    public @NotNull Map<String, Object> encode(final @NotNull UserGroup model) {
      return Map.of(
          "id", model.id().toString(),
          "userId", model.userId().toString(),
          "groupId", model.groupId().toString(),
          "createdAt", model.createdAt(),
          "duration", model.duration()
      );
    }

    @Override
    public @NotNull UserGroup decode(final @NotNull ResultSet resultSet) throws SQLException {
      return new UserGroup(
          UUID.fromString(resultSet.getString("id")),
          UUID.fromString(resultSet.getString("userId")),
          UUID.fromString(resultSet.getString("groupId")),
          resultSet.getLong("createdAt"),
          resultSet.getLong("duration")
      );
    }
  }
}

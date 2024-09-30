package me.espryth.groupsplugin.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a user.
 */
public final class User {

  private final UUID id;
  private final Map<UUID, UserGroup> groups;

  /**
   * Creates a new user.
   *
   * @param id The player's UUID.
   * @param groups The player's groups.
   */
  public User(final @NotNull UUID id, final @NotNull Collection<UserGroup> groups) {
    this.id = id;
    this.groups = groups.stream().collect(Collectors.toMap(UserGroup::groupId, Function.identity()));
  }

  public @NotNull UUID id() {
    return this.id;
  }

  public @NotNull Collection<UserGroup> groups() {
    return new ArrayList<>(this.groups.values());
  }

  public @Nullable UserGroup group(final @NotNull UUID groupId) {
    return this.groups.get(groupId);
  }

  @ApiStatus.Internal
  public @Nullable UserGroup removeGroup(final @NotNull UUID groupId) {
    return this.groups.remove(groupId);
  }

  @ApiStatus.Internal
  public void addGroup(final @NotNull UserGroup group) {
    this.groups.put(group.groupId(), group);
  }
}

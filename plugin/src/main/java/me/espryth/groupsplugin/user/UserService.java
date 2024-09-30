package me.espryth.groupsplugin.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.repository.Repository;
import me.espryth.groupsplugin.user.event.UserUpdateEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The user service.
 */
@Singleton
public final class UserService  {

  private final Map<UUID, User> users = new ConcurrentHashMap<>();
  private @Inject Repository<UserGroup> userGroupRepository;
  private @Inject GroupService groupService;
  private @Inject Executor executor;

  /**
   * Get the user model from the player.
   *
   * @param player The player to get the user from.
   * @return The user model.
   */
  public @NotNull User get(final @NotNull Player player) {
    return Objects.requireNonNull(this.users.get(player.getUniqueId()), "User not loaded");
  }

  /**
   * Get the user models from the online players.
   *
   * @return The user models.
   */
  public @NotNull Collection<User> getOnlineUsers() {
    return this.users.values();
  }

  @ApiStatus.Internal
  public void loadUser(final @NotNull User user) {
    this.users.put(user.id(), user);
  }

  @ApiStatus.Internal
  public void unloadUser(final @NotNull UUID userId) {
    this.users.remove(userId);
  }

  /**
   * Get the user model from the id,
   * if the user is not loaded it will get from the database.
   *
   * @param id The id to get the user from.
   * @return The user model.
   */
  public @NotNull CompletableFuture<@NotNull User> getOffline(final @NotNull UUID id) {
    final var cached = this.users.get(id);
    if (cached != null) {
      return CompletableFuture.completedFuture(cached);
    }
    return this.userGroupRepository.find("userId", id.toString()).thenApply(groups -> {
      final var groupsToAssign = new ArrayList<UserGroup>();
      for (final var group : groups) {
        if (group.isExpired()) {
          this.userGroupRepository.delete("id", group.id().toString());
        } else {
          groupsToAssign.add(group);
        }
      }
      final var user = new User(id, groupsToAssign);
      if (groupsToAssign.stream().noneMatch(group -> group.groupId().equals(this.groupService.getDefaultGroup().id()))) {
        final var defaultGroup = new UserGroup(user, this.groupService.getDefaultGroup(), Duration.ZERO);
        user.addGroup(defaultGroup);
        this.userGroupRepository.save(defaultGroup);
      }
      return user;
    });
  }

  /**
   * Get the user groups ordered by the group weight.
   *
   * @param user The user to get the groups from.
   * @return The user groups ordered by the group weight.
   */
  public @NotNull Stream<Group> getOrderedGroups(final @NotNull User user) {
    return user.groups()
        .stream()
        .map(userGroup -> this.groupService.getById(userGroup.groupId()))
        .filter(Objects::nonNull)
        .sorted();
  }

  /**
   * Get the highest group from the user.
   *
   * @param user The user to get the highest group from.
   * @return The highest group from the user.
   */
  public @NotNull Group getHighestGroup(final @NotNull User user) {
    return this.getOrderedGroups(user)
        .sorted()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Player has no groups"));
  }

  /**
   * Add a group to the user.
   *
   * @param userId The user id to add the group to.
   * @param group The group to add to the user.
   * @param duration The duration of the group.
   * @return A completable future that completes when the group is added to the user.
   */
  public @NotNull CompletableFuture<Void> addGroupToUser(final @NotNull UUID userId, final @NotNull Group group, final @NotNull Duration duration) {
    return this.getOffline(userId).thenAccept(user -> {
      final var userGroup = user.removeGroup(group.id());
      if (userGroup != null) {
        this.userGroupRepository.delete("id", userGroup.id().toString());
      }
      final var newUserGroup = new UserGroup(user, group, duration);
      this.userGroupRepository.save(newUserGroup).thenAcceptAsync(ignored -> {
        user.addGroup(newUserGroup);
        new UserUpdateEvent(user).callEvent();
      }, this.executor);
    });
  }

  /**
   * Remove a group from the user.
   *
   * @param userId The user id to remove the group from.
   * @param groupId The group id to remove from the user.
   * @return A completable future that completes when the group is removed from the user.
   */
  public @NotNull CompletableFuture<Boolean> removeGroupFromUser(final @NotNull UUID userId, final @NotNull UUID groupId) {
    return this.getOffline(userId).thenApply(user -> {
      final var userGroup = user.removeGroup(groupId);
      if (userGroup != null) {
        this.userGroupRepository.delete("id", userGroup.id().toString())
            .thenAcceptAsync(ignored -> new UserUpdateEvent(user).callEvent(), this.executor);
        return true;
      }
      return false;
    });
  }
}

package me.espryth.groupsplugin.group.permission;

import com.google.common.collect.ImmutableSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.user.User;
import me.espryth.groupsplugin.user.UserService;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for the PermissibleBase class.
 */
public final class PermissibleWrapper extends PermissibleBase {

  private final User user;
  private final UserService userService;
  private final GroupService groupService;

  /**
   * Constructor of the PermissibleWrapper class.
   *
   * @param player The player to inject the permissions.
   * @param userService The user service to get the user.
   * @param groupService The group service to get the permissions.
   */
  public PermissibleWrapper(
      final @NotNull Player player,
      final @NotNull UserService userService,
      final @NotNull GroupService groupService
  ) {
    super(player);
    this.user = userService.get(player);
    this.userService = userService;
    this.groupService = groupService;
  }

  @Override
  public synchronized void clearPermissions() {
  }

  @Override
  public synchronized void recalculatePermissions() {
  }

  @Override
  public boolean isPermissionSet(final @NotNull String name) {
    return this.userService.getOrderedGroups(user)
      .map(group -> this.groupService.getPermission(group, name.toLowerCase()))
      .anyMatch(Objects::nonNull);
  }

  @Override
  public boolean isPermissionSet(final @NotNull Permission perm) {
    return isPermissionSet(perm.getName().toLowerCase(Locale.ROOT));
  }

  @Override
  public boolean hasPermission(final @NotNull String inName) {
    for (final var permission : this.userService.getOrderedGroups(user)
        .map(group -> this.groupService.getPermission(group, inName.toLowerCase()))
        .toList()) {
      if (permission != null) {
        return permission.value();
      }
    }
    return false;
  }

  @Override
  public boolean hasPermission(final @NotNull Permission perm) {
    return hasPermission(perm.getName().toLowerCase(Locale.ROOT));
  }

  @Override
  public synchronized @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
    final var builder = ImmutableSet.<PermissionAttachmentInfo>builder();
    for (final var userGroup : user.groups()) {
      final var group = Objects.requireNonNull(this.groupService.getById(userGroup.groupId()));
      final var permissions = this.groupService.getPermissions(group);
      for (final var permission : permissions) {
        builder.add(
          new PermissionAttachmentInfo(
              this,
              permission.name(),
              null,
              permission.value()
          )
        );
      }
    }
    return builder.build();
  }
}

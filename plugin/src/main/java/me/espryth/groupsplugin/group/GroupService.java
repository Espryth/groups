package me.espryth.groupsplugin.group;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import me.espryth.groupsplugin.group.event.GroupCreateEvent;
import me.espryth.groupsplugin.group.event.GroupDeleteEvent;
import me.espryth.groupsplugin.group.event.GroupUpdateEvent;
import me.espryth.groupsplugin.group.permission.Permission;
import me.espryth.groupsplugin.repository.Repository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Service that manages groups and their permissions.
 */
@Singleton
public final class GroupService {

  private static final String DEFAULT_GROUP_NAME = "default";

  private final Map<UUID, Group> groups = new ConcurrentHashMap<>();
  private final Table<UUID, String, Permission> groupPermissions = HashBasedTable.create();
  private final Repository<Group> groupRepository;
  private final Repository<Permission> permissionRepository;
  private final Logger logger;

  private Group defaultGroup;

  @Inject
  GroupService(
      final @NotNull Repository<Group> groupRepository,
      final @NotNull Repository<Permission> permissionRepository,
      final @NotNull Logger logger
  ) {
    this.groupRepository = groupRepository;
    this.permissionRepository = permissionRepository;
    this.logger = logger;
    this.loadGroups();
  }

  private void loadGroups() {
    for (final var group : this.groupRepository.findAll().join()) {
      this.groups.put(group.id(), group);
      final var permissions = this.permissionRepository.find("groupId", group.id().toString()).join();
      for (final var permission : permissions) {
        this.groupPermissions.put(group.id(), permission.name(), permission);
      }
    }
    if (this.groups.isEmpty()) {
      this.logger.warn("No groups found in the database... Creating default group.");
      this.defaultGroup = Group.create(DEFAULT_GROUP_NAME);
      this.save(defaultGroup);
    } else {
      this.logger.info("Loaded {} groups from the database.", this.groups.size());
      this.defaultGroup = Objects.requireNonNull(this.getByName(DEFAULT_GROUP_NAME), "Default group not found.");
    }
  }

  /**
   * Get the default group.
   *
   * @return The default group.
   */
  public @NotNull Group getDefaultGroup() {
    return this.defaultGroup;
  }

  /**
   * Get a group by its id.
   *
   * @param id The group id.
   * @return The group or null if not found.
   */
  public @Nullable Group getById(final @NotNull UUID id) {
    return this.groups.get(id);
  }

  /**
   * Get a group by its name.
   *
   * @param name The group name.
   * @return The group or null if not found.
   */
  public @Nullable Group getByName(final @NotNull String name) {
    return this.groups.values().stream()
      .filter(group -> group.name().equalsIgnoreCase(name))
      .findFirst()
      .orElse(null);
  }

  /**
   * Get all groups.
   *
   * @return A collection ordered by the group's weight.
   */
  public @NotNull Collection<Group> getGroups() {
    final var copy = new ArrayList<>(this.groups.values());
    Collections.sort(copy);
    return copy;
  }

  /**
   * Get the permission of a group.
   *
   * @param group The group.
   * @param permissionName The permission name.
   * @return The permission or null if not found.
   */
  public @Nullable Permission getPermission(final @NotNull Group group, final @NotNull String permissionName) {
    final var all = this.groupPermissions.get(group.id(), "*");
    if (all != null) {
      return all;
    }
    return this.groupPermissions.get(group.id(), permissionName);
  }

  /**
   * Get all permissions of a group.
   *
   * @param group The group.
   * @return A collection of permissions.
   */
  public @NotNull Collection<Permission> getPermissions(final @NotNull Group group) {
    return this.groupPermissions.row(group.id()).values();
  }

  /**
   * Set a permission to a group.
   *
   * @param group The group.
   * @param permissionName The permission name.
   * @param value The permission value.
   */
  public void setPermission(final @NotNull Group group, final @NotNull String permissionName, final boolean value) {
    final var permissionNameLower = permissionName.toLowerCase();
    final var permission = this.groupPermissions.get(group.id(), permissionNameLower);
    if (permission == null) {
      final var permissionToSet = Permission.create(group, permissionNameLower, value);
      this.groupPermissions.put(group.id(), permissionNameLower, permissionToSet);
      this.permissionRepository.save(permissionToSet);
      return;
    }
    if (permission.value() != value) {
      permission.value(value);
      this.permissionRepository.save(permission);
    }
  }

  /**
   * Remove a permission from a group.
   *
   * @param group The group.
   * @param permissionName The permission name.
   */
  public void removePermission(final @NotNull Group group, final @NotNull String permissionName) {
    final var permission = this.groupPermissions.remove(group.id(), permissionName);
    if (permission != null) {
      this.permissionRepository.delete("id", permission.id().toString());
    }
  }

  /**
   * Save a group this method calls
   * {@link GroupCreateEvent} if the group is new
   * or {@link GroupUpdateEvent} if the group is updated.
   *
   * @param group The group to save.
   */
  public void save(final @NotNull Group group) {
    final var create = new AtomicBoolean(false);
    if (!this.groups.containsKey(group.id())) {
      create.set(true);
    }
    this.groupRepository.save(group).whenComplete((result, throwable) -> {
      if (throwable != null) {
        this.logger.error("Failed to save group with id {}.", group.id(), throwable);
        return;
      }
      if (create.get()) {
        this.groups.put(group.id(), group);
        this.logger.info("Group with id {} created.", group.id());
        new GroupCreateEvent(group).callEvent();
      } else {
        this.logger.info("Group with id {} updated.", group.id());
        new GroupUpdateEvent(group).callEvent();
      }
    });
  }

  /**
   * Delete a group this method calls
   * {@link GroupDeleteEvent} if the group is deleted.
   *
   * @param group The group to delete.
   */
  public void delete(final @NotNull Group group) {
    if (group.name().equalsIgnoreCase(DEFAULT_GROUP_NAME)) {
      this.logger.error("You can't delete the default group.");
      return;
    }
    this.groupRepository.delete("id", group.id().toString()).whenComplete((result, throwable) -> {
      if (throwable != null) {
        this.logger.error("Failed to delete group with id {}.", group.id(), throwable);
        return;
      }
      this.groups.remove(group.id());
      this.groupPermissions.row(group.id()).clear();
      this.logger.info("Group with id {} deleted.", group.id());
      new GroupDeleteEvent(group).callEvent();
    });
  }
}

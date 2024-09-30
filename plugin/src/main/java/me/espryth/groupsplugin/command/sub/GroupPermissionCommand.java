package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.OptArg;

/**
 * A command to manage group permissions.
 */
@Command(names = {"perm", "permission"})
public class GroupPermissionCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Sets a permission to a group.
   *
   * @param sender The sender of the command.
   * @param group The group to set the permission.
   * @param permission The permission to set.
   * @param value The value of the permission (default is true).
   */
  @Command(names = "set")
  public void set(final CommandSender sender, final Group group, final String permission, final @OptArg("true") boolean value) {
    this.groupService.setPermission(group, permission, value);
    Lang.send(sender, "group.permission.set", "group", group.name(), "permission", permission, "value", value);
  }

  /**
   * Removes a permission from a group.
   *
   * @param sender The sender of the command.
   * @param group The group to remove the permission.
   * @param permission The permission to remove.
   */
  @Command(names = "remove")
  public void remove(final CommandSender sender, final Group group, final String permission) {
    this.groupService.removePermission(group, permission);
    Lang.send(sender, "group.permission.remove", "group", group.name(), "permission", permission);
  }

}

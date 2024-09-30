package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * Command to get information about a group.
 */
public class GroupInfoCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param group The group to get information about.
   */
  @Command(names = "info")
  public void run(final CommandSender sender, final Group group) {
    Lang.send(sender, "group.info.message", group.tagResolver());
    for (final var permission : this.groupService.getPermissions(group)) {
      Lang.send(sender, "group.info.permission", permission.tagResolver());
    }
  }
}

package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * Command to rename a group.
 */
public class GroupRenameCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param group The group to rename.
   * @param name The new name of the group.
   */
  @Command(names = "rename")
  public void run(final CommandSender sender, final Group group, final String name) {
    if (this.groupService.getDefaultGroup().equals(group)) {
      Lang.send(sender, "group.rename.default");
      return;
    }
    final var lowerName = name.toLowerCase();
    Lang.send(sender, "group.rename.success", "old", group.name(), "new", lowerName);
    group.name(lowerName);
    this.groupService.save(group);
  }
}

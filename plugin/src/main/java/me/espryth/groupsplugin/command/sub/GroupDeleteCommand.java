package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * A command to delete a group.
 */
public class GroupDeleteCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param group The group to delete.
   */
  @Command(names = "delete")
  public void run(final CommandSender sender, final Group group) {
    if (this.groupService.getDefaultGroup().equals(group)) {
      Lang.send(sender, "group.delete.default");
      return;
    }
    Lang.send(sender, "group.delete.success", "group", group.name());
    this.groupService.delete(group);
  }

}

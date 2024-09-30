package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * A command to list all groups.
 */
public class GroupListCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   */
  @Command(names = "list")
  public void run(final CommandSender sender) {
    final var groups = groupService.getGroups();
    Lang.send(sender, "group.list.header", "size", groups.size());
    for (final var group : groups) {
      Lang.send(sender, "group.list.entry", group.tagResolver());
    }
  }
}

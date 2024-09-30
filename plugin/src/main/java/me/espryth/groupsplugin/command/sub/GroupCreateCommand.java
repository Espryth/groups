package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * A command to create a group.
 */
public class GroupCreateCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param name The name of the group.
   */
  @Command(names = "create")
  public void run(final CommandSender sender, final String name) {
    final var lowerName = name.toLowerCase();
    final var group = this.groupService.getByName(lowerName);
    if (group == null) {
      this.groupService.save(Group.create(lowerName));
      Lang.send(sender, "group.create.success", "name", lowerName);
    } else {
      Lang.send(sender, "group.create.error", "name", lowerName);
    }
  }
}

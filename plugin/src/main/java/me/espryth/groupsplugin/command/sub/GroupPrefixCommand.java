package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Text;

/**
 * A command to set the prefix of a group.
 */
public class GroupPrefixCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param group The group to set the prefix.
   * @param prefix The prefix to set.
   */
  @Command(names = "prefix")
  public void run(final CommandSender sender, final Group group, final @Text String prefix) {
    group.prefix(prefix);
    this.groupService.save(group);
    Lang.send(sender, "group.prefix", "prefix", group.prefix());
  }
}

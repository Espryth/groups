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
 * Command to set the suffix of a group.
 */
public class GroupSuffixCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param group The group to set the suffix.
   * @param suffix The suffix to set.
   */
  @Command(names = "suffix")
  public void run(final CommandSender sender, final Group group, final @Text String suffix) {
    group.suffix(suffix);
    Lang.send(sender, "group.suffix", "suffix", group.suffix());
    this.groupService.save(group);
  }
}

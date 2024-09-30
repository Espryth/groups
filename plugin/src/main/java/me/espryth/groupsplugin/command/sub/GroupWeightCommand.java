package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * Command to set the weight of a group.
 */
public class GroupWeightCommand implements CommandClass {

  private @Inject GroupService groupService;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param group The group to set the weight.
   * @param weight The weight to set.
   */
  @Command(names = "weight")
  public void run(final CommandSender sender, final Group group, final int weight) {
    if (weight < 0 || weight > 100) {
      Lang.send(sender, "group.weight.invalid");
      return;
    }
    group.weight(weight);
    this.groupService.save(group);
    Lang.send(sender, "group.weight.set", "group", group.name(), "weight", weight);
  }
}

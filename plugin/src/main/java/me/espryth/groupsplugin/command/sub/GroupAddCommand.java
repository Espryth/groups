package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import java.time.Duration;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import me.espryth.groupsplugin.user.UserService;
import me.espryth.groupsplugin.util.DurationFormatter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.slf4j.Logger;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.OptArg;

/**
 * A command to add a group to a player.
 */
public class GroupAddCommand implements CommandClass {

  private @Inject UserService userService;
  private @Inject GroupService groupService;
  private @Inject Logger logger;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param player The player to add the group to.
   * @param group The group to add to the player.
   * @param duration The duration of the group or empty if it's permanent.
   */
  @Command(names = "add")
  public void run(final CommandSender sender, final OfflinePlayer player, final Group group, final @OptArg("") Duration duration) {
    if (this.groupService.getDefaultGroup().equals(group)) {
      sender.sendMessage(Lang.translatable("group.add.default"));
      return;
    }
    this.userService.addGroupToUser(player.getUniqueId(), group, duration).whenComplete((user, throwable) -> {
      if (throwable != null) {
        Lang.send(sender, "group.add.error", "player", player.getName(), "group", group.name());
        logger.error("An error occurred while adding a group to a user", throwable);
        return;
      }
      Lang.send(sender, "group.add.success", "player", player.getName(), "group", group.name(), "time", DurationFormatter.format(duration));
    });
  }
}

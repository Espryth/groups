package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import me.espryth.groupsplugin.user.UserService;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.slf4j.Logger;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;


/**
 * Command to remove a group from a player.
 */
public class GroupRemoveCommand implements CommandClass {

  private @Inject UserService userService;
  private @Inject GroupService groupService;
  private @Inject Logger logger;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param player The player to remove the group from.
   * @param group The group to remove from the player.
   */
  @Command(names = "remove")
  public void run(final CommandSender sender, final OfflinePlayer player, final Group group) {
    if (this.groupService.getDefaultGroup().equals(group)) {
      Lang.send(sender, "group.remove.default");
      return;
    }
    this.userService.removeGroupFromUser(player.getUniqueId(), group.id()).whenComplete((result, throwable) -> {
      if (throwable != null) {
        Lang.send(sender, "group.remove.error");
        logger.error("An error occurred while removing a group from a user", throwable);
        return;
      }
      if (result) {
        Lang.send(sender, "group.remove.success", "player", player.getName(), "group", group.name());
      } else {
        Lang.send(sender, "group.remove.not-in", "player", player.getName(), "group", group.name());
      }
    });
  }
}

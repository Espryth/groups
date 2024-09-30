package me.espryth.groupsplugin.command.sub;

import com.google.inject.Inject;
import java.time.Duration;
import java.util.Objects;
import me.espryth.groupsplugin.lang.Lang;
import me.espryth.groupsplugin.user.UserService;
import me.espryth.groupsplugin.util.DurationFormatter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.slf4j.Logger;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;

/**
 * A command to check the groups of a player.
 */
public class GroupCheckCommand implements CommandClass {

  private @Inject UserService userService;
  private @Inject Logger logger;

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param player The player to check the groups.
   */
  @Command(names = "check")
  public void run(final CommandSender sender, final OfflinePlayer player) {
    this.userService.getOffline(player.getUniqueId()).whenComplete((user, throwable) -> {
      if (throwable != null) {
        Lang.send(sender, "group.check.error", "player", player.getName());
        logger.error("An error occurred while checking the groups of the player {}", player.getName(), throwable);
        return;
      }
      final var groups = this.userService.getOrderedGroups(user).toList();
      Lang.send(sender, "group.check.message.header", "player", player.getName(), "groups", groups.size());
      for (final var group : groups) {
        final var userGroup = Objects.requireNonNull(user.group(group.id()));
        final var left = Duration.ofMillis(userGroup.createdAt() + userGroup.duration() - System.currentTimeMillis());
        Lang.send(sender, "group.check.message.group", "group", group.name(), "left", DurationFormatter.format(left));
      }
    });
  }
}

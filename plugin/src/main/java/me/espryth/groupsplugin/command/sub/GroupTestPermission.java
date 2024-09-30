package me.espryth.groupsplugin.command.sub;

import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.entity.Player;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Sender;

/**
 * Command to test a permission using {@link Player#hasPermission(String)}.
 */
public class GroupTestPermission implements CommandClass {

  /**
   * Executes the command.
   *
   * @param sender The sender of the command.
   * @param permission The permission to test.
   */
  @Command(names = "testperm")
  public void run(final @Sender Player sender, final String permission) {
    if (sender.hasPermission(permission)) {
      Lang.send(sender, "group.permission-test.success", "permission", permission);
    } else {
      Lang.send(sender, "group.permission-test.failure", "permission", permission);
    }
  }
}

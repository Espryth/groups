package me.espryth.groupsplugin.command;

import me.espryth.groupsplugin.command.sub.GroupAddCommand;
import me.espryth.groupsplugin.command.sub.GroupCheckCommand;
import me.espryth.groupsplugin.command.sub.GroupCreateCommand;
import me.espryth.groupsplugin.command.sub.GroupDeleteCommand;
import me.espryth.groupsplugin.command.sub.GroupInfoCommand;
import me.espryth.groupsplugin.command.sub.GroupListCommand;
import me.espryth.groupsplugin.command.sub.GroupPermissionCommand;
import me.espryth.groupsplugin.command.sub.GroupPrefixCommand;
import me.espryth.groupsplugin.command.sub.GroupRemoveCommand;
import me.espryth.groupsplugin.command.sub.GroupRenameCommand;
import me.espryth.groupsplugin.command.sub.GroupSuffixCommand;
import me.espryth.groupsplugin.command.sub.GroupTestPermission;
import me.espryth.groupsplugin.command.sub.GroupWeightCommand;
import me.espryth.groupsplugin.lang.Lang;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.SubCommandClasses;

/**
 * The main group command.
 */
@SubCommandClasses({
  GroupCreateCommand.class,
  GroupDeleteCommand.class,
  GroupAddCommand.class,
  GroupRemoveCommand.class,
  GroupRenameCommand.class,
  GroupPermissionCommand.class,
  GroupCheckCommand.class,
  GroupInfoCommand.class,
  GroupPrefixCommand.class,
  GroupSuffixCommand.class,
  GroupListCommand.class,
  GroupWeightCommand.class,
  GroupTestPermission.class
})
@Command(names = "group")
public class GroupCommand implements CommandClass {

  /**
   * Sends the help message to the sender.
   *
   * @param sender The sender of the command.
   */
  @Command(names = {"", "help"})
  public void help(final CommandSender sender) {
    Lang.send(sender, "group.help");
  }
}

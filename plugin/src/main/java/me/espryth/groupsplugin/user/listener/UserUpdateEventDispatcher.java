package me.espryth.groupsplugin.user.listener;

import com.google.inject.Inject;
import java.util.HashSet;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.event.GroupDeleteEvent;
import me.espryth.groupsplugin.group.event.GroupUpdateEvent;
import me.espryth.groupsplugin.user.User;
import me.espryth.groupsplugin.user.UserService;
import me.espryth.groupsplugin.user.event.UserUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * This class is responsible for dispatching the UserUpdateEvent when a group is updated or deleted.
 */
public class UserUpdateEventDispatcher implements Listener {

  private @Inject UserService userService;

  @EventHandler
  void onGroupDelete(final GroupDeleteEvent event) {
    callEvent(event.group());
  }

  @EventHandler
  void onGroupUpdate(final GroupUpdateEvent event) {
    callEvent(event.group());
  }

  private void callEvent(final @NotNull Group group) {
    final var usersToUpdate = new HashSet<User>();
    for (final var user : userService.getOnlineUsers()) {
      if (user.group(group.id()) != null) {
        usersToUpdate.add(user);
      }
    }
    for (final var user : usersToUpdate) {
      new UserUpdateEvent(user).callEvent();
    }
  }
}

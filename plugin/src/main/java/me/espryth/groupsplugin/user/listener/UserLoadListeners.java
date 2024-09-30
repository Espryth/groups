package me.espryth.groupsplugin.user.listener;

import com.google.inject.Inject;
import me.espryth.groupsplugin.user.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.slf4j.Logger;

/**
 * This class is responsible for loading and unloading user data.
 */
public class UserLoadListeners implements Listener {

  private @Inject UserService userService;
  private @Inject Logger logger;

  @EventHandler
  void onJoin(final PlayerLoginEvent event) {
    this.userService.getOffline(event.getPlayer().getUniqueId()).whenComplete((user, throwable) -> {
      if (throwable != null) {
        this.logger.error("An error occurred while loading user data", throwable);
        return;
      }
      this.userService.loadUser(user);
    });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  void onQuit(final PlayerQuitEvent event) {
    this.userService.unloadUser(event.getPlayer().getUniqueId());
  }

}

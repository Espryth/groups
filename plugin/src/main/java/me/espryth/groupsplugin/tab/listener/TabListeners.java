package me.espryth.groupsplugin.tab.listener;

import com.google.inject.Inject;
import java.util.Objects;
import me.espryth.groupsplugin.tab.TabService;
import me.espryth.groupsplugin.user.event.UserUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class is responsible for assigning the tab and updating the tab when the user is updated.
 */
public class TabListeners implements Listener {

  private final TabService tabService;

  @Inject
  public TabListeners(final TabService tabService) {
    this.tabService = tabService;
  }

  @EventHandler
  void onUserUpdate(final UserUpdateEvent event) {
    final var user = event.user();
    final var player = Bukkit.getPlayer(user.id());
    this.tabService.updateUser(Objects.requireNonNull(player));
  }

  @EventHandler
  void onJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    this.tabService.setupTab(player);
  }

  @EventHandler
  void onQuit(final PlayerQuitEvent event) {
    final var player = event.getPlayer();
    this.tabService.removeTab(player);
  }
}

package me.espryth.groupsplugin.listener;

import com.google.inject.Inject;
import me.espryth.groupsplugin.lang.Lang;
import me.espryth.groupsplugin.user.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Changes the join and quit messages.
 */
public class PlayerJoinQuitListener implements Listener {

  private @Inject UserService userService;

  @EventHandler
  void onJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var user = this.userService.get(player);
    final var group = this.userService.getHighestGroup(user);
    event.joinMessage(
        Lang.translatable(
            "join-message",
            "prefix", group.prefix(),
            "suffix", group.suffix(),
            "player", player.getName()
        )
    );
  }

  @EventHandler
  void onQuit(final PlayerQuitEvent event) {
    final var player = event.getPlayer();
    final var user = this.userService.get(player);
    final var group = this.userService.getHighestGroup(user);
    event.quitMessage(
        Lang.translatable(
            "quit-message",
            "prefix", group.prefix(),
            "suffix", group.suffix(),
            "player", player.getName()
        )
    );
  }
}

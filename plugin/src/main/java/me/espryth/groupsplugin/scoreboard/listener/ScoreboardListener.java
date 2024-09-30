package me.espryth.groupsplugin.scoreboard.listener;

import com.google.inject.Inject;
import me.espryth.groupsplugin.scoreboard.ScoreboardService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class is responsible for assigning and removing the scoreboard from the player.
 */
public class ScoreboardListener implements Listener {

  private @Inject ScoreboardService scoreboardService;

  @EventHandler
  void onJoin(final PlayerJoinEvent event) {
    scoreboardService.setScoreboard(event.getPlayer());
  }

  @EventHandler
  void onQuit(final PlayerQuitEvent event) {
    scoreboardService.removeScoreboard(event.getPlayer());
  }

}

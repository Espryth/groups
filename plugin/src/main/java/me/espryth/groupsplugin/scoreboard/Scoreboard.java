package me.espryth.groupsplugin.scoreboard;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A player scoreboard.
 */
public final class Scoreboard {

  private final String id;
  private final WeakReference<Player> viewer;
  private final Supplier<Component> title;
  private final Supplier<List<Component>> lines;

  private boolean destroyed;

  Scoreboard(
      final @NotNull Player viewer,
      final @NotNull Supplier<Component> title,
      final @NotNull Supplier<List<Component>> lines
  ) {
    this.title = title;
    this.lines = lines;
    this.id = "sb-" + Integer.toHexString(ThreadLocalRandom.current().nextInt());
    this.viewer = new WeakReference<>(viewer);
    this.destroyed = false;
    update(true);
  }

  /**
   * Update the scoreboard title and lines.
   */
  public void update() {
    this.update(false);
  }

  /**
   * Update the scoreboard title and lines.
   *
   * @param create if the scoreboard should be created.
   */
  private void update(final boolean create) {
    if (destroyed) {
      return;
    }
    final var lines = this.lines.get();
    if (create) {
      ScoreboardHandler.setObjective(this, ClientboundSetObjectivePacket.METHOD_ADD);
      ScoreboardHandler.setDisplayObjective(this);
    }
    ScoreboardHandler.setObjective(this, ClientboundSetObjectivePacket.METHOD_CHANGE);
    for (int i = 0; i < lines.size(); i++) {
      final var line = lines.get(i);
      if (create) {
        ScoreboardHandler.setTeam(this, ClientboundSetPlayerTeamPacket.Action.ADD, line, i, true);
        ScoreboardHandler.setScore(this, i);
      }
      ScoreboardHandler.setTeam(this, ClientboundSetPlayerTeamPacket.Action.ADD, line, i, false);
    }
  }

  /**
   * Destroy the scoreboard.
   */
  public void destroy() {
    if (destroyed) {
      return;
    }
    final var lines = this.lines.get();
    for (int i = 0; i < lines.size(); i++) {
      ScoreboardHandler.setTeam(this, ClientboundSetPlayerTeamPacket.Action.REMOVE, null, i, false);
    }
    ScoreboardHandler.setObjective(this, ClientboundSetObjectivePacket.METHOD_REMOVE);
    destroyed = true;
  }

  /**
   * Get the scoreboard id.
   *
   * @return the scoreboard id.
   */
  public @NotNull String id() {
    return this.id;
  }

  /**
   * Get the viewer of the scoreboard.
   *
   * @return the viewer of the scoreboard.
   */
  public @NotNull Player viewer() {
    return Objects.requireNonNull(this.viewer.get());
  }

  /**
   * Get the scoreboard title.
   *
   * @return the scoreboard title.
   */
  public @NotNull Component title() {
    return this.title.get();
  }

  /**
   * Get the scoreboard lines.
   *
   * @return the scoreboard lines.
   */
  public @NotNull List<Component> lines() {
    return this.lines.get();
  }
}

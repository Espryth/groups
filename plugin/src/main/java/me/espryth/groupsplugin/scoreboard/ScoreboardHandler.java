package me.espryth.groupsplugin.scoreboard;

import io.papermc.paper.adventure.AdventureComponent;
import java.util.ArrayList;
import java.util.Optional;
import me.espryth.groupsplugin.packet.PacketSender;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is responsible for handling the scoreboard packets.
 */
@SuppressWarnings("deprecation")
final class ScoreboardHandler {

  /**
   * Set the objective of the scoreboard.
   *
   * @param scoreboard The scoreboard to set the objective.
   * @param mode The mode of the objective.
   */
  static void setObjective(final Scoreboard scoreboard, final int mode) {
    PacketSender.send(
        scoreboard.viewer(),
        new ClientboundSetObjectivePacket(
            new Objective(
                new net.minecraft.world.scores.Scoreboard(),
                scoreboard.id(),
                ObjectiveCriteria.DUMMY,
                new AdventureComponent(scoreboard.title()),
                ObjectiveCriteria.RenderType.HEARTS,
                false,
                null
            ),
            mode
        )
    );
  }

  /**
   * Set the display objective of the scoreboard.
   *
   * @param scoreboard The scoreboard to set the display objective.
   */
  static void setDisplayObjective(final @NotNull Scoreboard scoreboard) {
    PacketSender.send(
        scoreboard.viewer(),
        new ClientboundSetDisplayObjectivePacket(
            DisplaySlot.SIDEBAR,
            new Objective(
                new net.minecraft.world.scores.Scoreboard(),
                scoreboard.id(),
                ObjectiveCriteria.AIR,
                Component.empty(),
                ObjectiveCriteria.RenderType.INTEGER,
                false,
                null
            )
        )
    );
  }

  /**
   * Set the score of the scoreboard.
   *
   * @param scoreboard The scoreboard to set the score.
   * @param score The score to set.
   */
  static void setScore(final @NotNull Scoreboard scoreboard, final int score) {
    PacketSender.send(
        scoreboard.viewer(),
        new ClientboundSetScorePacket(
            ChatColor.values()[score].toString(),
            scoreboard.id(),
            score,
            Optional.empty(),
            Optional.empty()
        )
    );
  }

  /**
   * Set the team of the scoreboard.
   *
   * @param scoreboard The scoreboard to set the team.
   * @param action The action of the team.
   * @param text The text to show.
   * @param score The score to set.
   * @param create If the team should be created.
   */
  static void setTeam(
      final @NotNull Scoreboard scoreboard,
      final @NotNull ClientboundSetPlayerTeamPacket.Action action,
      final @Nullable net.kyori.adventure.text.Component text,
      final int score,
      final boolean create
  ) {
    final var playerTeam = new PlayerTeam(new net.minecraft.world.scores.Scoreboard(), scoreboard.id() + ":" + score);
    if (action == ClientboundSetPlayerTeamPacket.Action.ADD) {
      final var packets = new ArrayList<Packet<? super ClientGamePacketListener>>();
      playerTeam.getPlayers().add(ChatColor.values()[score].toString());
      playerTeam.setNameTagVisibility(Team.Visibility.ALWAYS);
      playerTeam.setPlayerPrefix(new AdventureComponent(text));
      if (create) {
        packets.add(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));
      }
      packets.add(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, false));
      PacketSender.send(scoreboard.viewer(), packets);
    } else {
      PacketSender.send(
          scoreboard.viewer(),
          ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeam)
      );
    }
  }
}

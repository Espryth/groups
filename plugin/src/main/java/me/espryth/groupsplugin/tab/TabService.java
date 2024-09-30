package me.espryth.groupsplugin.tab;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.papermc.paper.adventure.AdventureComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.espryth.groupsplugin.packet.PacketSender;
import me.espryth.groupsplugin.user.UserService;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Service to manage the tab list name tags.
 */
@Singleton
public final class TabService {

  private final Map<UUID, PlayerTeam> playerTeams = new ConcurrentHashMap<>();
  private @Inject UserService userService;

  /**
   * Set up the tab list for the player.
   *
   * @param player The player.
   */
  public void setupTab(final @NotNull Player player) {
    this.updateUser(player);
    final var onlinePlayers = Bukkit.getOnlinePlayers();
    final var packets = new ArrayList<Packet<? super ClientGamePacketListener>>(onlinePlayers.size() * 2);
    for (final var onlinePlayer : onlinePlayers) {
      final var team = this.playerTeams.get(onlinePlayer.getUniqueId());
      if (team != null) {
        packets.addAll(this.createTeamPackets(onlinePlayer, team));
      }
    }
    PacketSender.send(player, packets);
  }

  /**
   * Remove the tab list for the player.
   *
   * @param player The player.
   */
  public void removeTab(final @NotNull Player player) {
    final var team = this.playerTeams.remove(player.getUniqueId());
    if (team != null) {
      final var packet = ClientboundSetPlayerTeamPacket.createRemovePacket(team);
      Bukkit.getOnlinePlayers().forEach(onlinePlayer -> PacketSender.send(onlinePlayer, packet));
    }
  }

  /**
   * Update the user tab list.
   *
   * @param player The player.
   */
  public void updateUser(final @NotNull Player player) {
    final var user = this.userService.get(player);
    final var group = this.userService.getHighestGroup(user);
    final var previousTeam = this.playerTeams.get(user.id());
    final var packets = new ArrayList<Packet<? super ClientGamePacketListener>>(2);
    if (previousTeam != null) {
      packets.add(ClientboundSetPlayerTeamPacket.createRemovePacket(previousTeam));
    }
    final var team = new PlayerTeam(new Scoreboard(), TabWeight.get(group.weight()) + player.getName());
    team.setPlayerPrefix(new AdventureComponent(group.prefix()));
    team.setPlayerSuffix(new AdventureComponent(group.suffix()));
    packets.addAll(this.createTeamPackets(player, team));
    this.playerTeams.put(user.id(), team);
    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> PacketSender.send(onlinePlayer, packets));
  }

  private List<Packet<? super ClientGamePacketListener>> createTeamPackets(final @NotNull Player player, final @NotNull PlayerTeam playerTeam) {
    return List.of(
      ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true),
      ClientboundSetPlayerTeamPacket.createPlayerPacket(playerTeam, player.getName(), ClientboundSetPlayerTeamPacket.Action.ADD)
    );
  }
}

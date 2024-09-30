package me.espryth.groupsplugin.packet;

import java.util.Arrays;
import java.util.Collection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to send packets to players.
 */
public final class PacketSender {

  private PacketSender() {
  }

  /**
   * Sends a packet to a player.
   *
   * @param player The player to send the packet to.
   * @param packet The packet to send.
   */
  public static void send(final @NotNull Player player, final @NotNull Packet<?> packet) {
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }

  /**
   * Sends multiple packets to a player.
   *
   * @param player The player to send the packets to.
   * @param packets The packets to send.
   */
  @SuppressWarnings("unchecked")
  public static void send(final @NotNull Player player, final @NotNull Packet<ClientGamePacketListener>... packets) {
    if (packets.length == 0) {
      return;
    }
    final var packet = packets.length == 1 ? packets[0] : new ClientboundBundlePacket(Arrays.asList(packets));
    send(player, packet);
  }

  /**
   * Sends multiple packets to a player.
   *
   * @param player The player to send the packets to.
   * @param packets The packets to send.
   */
  public static void send(final Player player, final @NotNull Collection<Packet<? super ClientGamePacketListener>> packets) {
    if (packets.isEmpty()) {
      return;
    }
    final var packet = packets.size() == 1 ? packets.iterator().next() : new ClientboundBundlePacket(packets);
    send(player, packet);
  }
}

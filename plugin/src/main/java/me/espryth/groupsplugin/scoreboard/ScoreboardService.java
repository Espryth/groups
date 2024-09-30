package me.espryth.groupsplugin.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.espryth.groupsplugin.lang.Lang;
import me.espryth.groupsplugin.lang.translator.LangResourceProvider;
import me.espryth.groupsplugin.user.UserService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A service to manage the scoreboards.
 */
@Singleton
public final class ScoreboardService {

  private final Map<UUID, Scoreboard> scoreboards = new ConcurrentHashMap<>();

  private final UserService userService;

  @Inject
  ScoreboardService(final Plugin plugin, final UserService userService) {
    this.userService = userService;
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> this.scoreboards.values().forEach(Scoreboard::update), 0, 20);
  }

  /**
   * Assign a scoreboard to a player to shows
   * the player's information.
   *
   * @param player the player to assign the scoreboard to.
   */
  public void setScoreboard(final @NotNull Player player) {
    this.scoreboards.put(
        player.getUniqueId(),
        new Scoreboard(
            player,
            () -> Lang.translate(player, Lang.translatable("scoreboard.title")),
            () -> {
              final var user = this.userService.get(player);
              final var group = this.userService.getHighestGroup(user);
              final var resolvers = TagResolver.resolver(
                  TagResolver.resolver("player", Tag.selfClosingInserting(player.displayName())),
                  TagResolver.resolver("prefix", Tag.selfClosingInserting(group.prefix())),
                  TagResolver.resolver("suffix", Tag.selfClosingInserting(group.suffix()))
              );
              return LangResourceProvider.getTranslator()
                  .getFile(player.locale())
                  .getStringList("scoreboard.lines")
                  .reversed()
                  .stream()
                  .map(line -> MiniMessage.miniMessage().deserialize(line, resolvers))
                  .toList();
            })
    );
  }

  /**
   * Remove the scoreboard from a player.
   *
   * @param player the player to remove the scoreboard from.
   */
  public void removeScoreboard(final @NotNull Player player) {
    this.scoreboards.remove(player.getUniqueId()).destroy();
  }
}

package me.espryth.groupsplugin.listener;

import com.google.inject.Inject;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.espryth.groupsplugin.lang.Lang;
import me.espryth.groupsplugin.user.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Changes the chat format based on the player's group.
 */
public class ChatListener implements Listener {

  private @Inject UserService userService;

  @EventHandler
  void onChat(final AsyncChatEvent event) {
    final var user = this.userService.get(event.getPlayer());
    final var group = this.userService.getHighestGroup(user);
    event.renderer((sender, displayName, message, viewer) ->
        Lang.translate(
            viewer,
            Lang.translatable(
                "chat-format",
        "message", message,
                "sender", displayName,
                "prefix", group.prefix(),
                "suffix", group.suffix()
            )
        )
    );
  }
}

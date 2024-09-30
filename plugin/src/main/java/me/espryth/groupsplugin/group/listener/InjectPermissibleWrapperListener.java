package me.espryth.groupsplugin.group.listener;

import com.google.inject.Inject;
import java.lang.reflect.Field;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.group.permission.PermissibleWrapper;
import me.espryth.groupsplugin.user.UserService;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissibleBase;
import org.slf4j.Logger;

/**
 * Injects a {@link PermissibleWrapper} to the player when they join the server.
 */
public class InjectPermissibleWrapperListener implements Listener {

  private static final Field HUMAN_ENTITY_PERMISSIBLE_FIELD;

  static {
    try {
      HUMAN_ENTITY_PERMISSIBLE_FIELD = CraftHumanEntity.class.getDeclaredField("perm");
      HUMAN_ENTITY_PERMISSIBLE_FIELD.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private @Inject UserService userService;
  private @Inject GroupService groupService;
  private @Inject Logger logger;

  @EventHandler
  void onJoin(final PlayerJoinEvent event) {
    final var player = event.getPlayer();
    try {
      final var permissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(event.getPlayer());
      if (permissible instanceof PermissibleWrapper) {
        return;
      }
      HUMAN_ENTITY_PERMISSIBLE_FIELD.set(
          event.getPlayer(),
          new PermissibleWrapper(player, this.userService, this.groupService)
      );
    } catch (IllegalAccessException e) {
      this.logger.error("Error while injecting PermissibleWrapper to user {}", player.getName(), e);
    }
  }
}

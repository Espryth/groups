package me.espryth.groupsplugin.user.event;

import me.espryth.groupsplugin.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the user groups are updated or modified.
 */
public final class UserUpdateEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final User user;

  public UserUpdateEvent(final User user) {
    super(true);
    this.user = user;
  }

  public @NotNull User user() {
    return user;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLERS;
  }
}

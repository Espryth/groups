package me.espryth.groupsplugin.group.event;

import me.espryth.groupsplugin.group.Group;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a group is deleted.
 */
public final class GroupDeleteEvent extends GroupEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  public GroupDeleteEvent(final @NotNull Group group) {
    super(group);
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLERS;
  }
}

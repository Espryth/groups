package me.espryth.groupsplugin.group.event;

import me.espryth.groupsplugin.group.Group;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when any group property is updated (name, prefix, suffix, weight).
 */
public final class GroupUpdateEvent extends GroupEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  public GroupUpdateEvent(final @NotNull Group group) {
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

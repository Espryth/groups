package me.espryth.groupsplugin.group.event;

import me.espryth.groupsplugin.group.Group;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a group is created.
 */
public final class GroupCreateEvent extends GroupEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  public GroupCreateEvent(final @NotNull Group group) {
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

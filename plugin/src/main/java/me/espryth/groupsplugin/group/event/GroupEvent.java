package me.espryth.groupsplugin.group.event;

import me.espryth.groupsplugin.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a group event.
 */
public abstract class GroupEvent extends Event {

  private final Group group;

  protected GroupEvent(final @NotNull Group group) {
    super(true);
    this.group = group;
  }

  /**
   * Returns the group of this event.
   *
   * @return The group of this event.
   */
  public @NotNull Group group() {
    return group;
  }
}

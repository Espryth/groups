package me.espryth.groupsplugin.util;

import java.time.Duration;
import me.espryth.groupsplugin.lang.Lang;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class to format a {@link Duration}.
 */
public class DurationFormatter {

  /**
   * Format a {@link Duration} to a {@link TranslatableComponent}.
   *
   * @param duration The duration to format.
   * @return The formatted duration.
   */
  public static @NotNull TranslatableComponent format(final @NotNull Duration duration) {
    if (duration.isZero() || duration.isNegative()) {
      return Lang.translatable("duration.permanent");
    }
    final var days = duration.toDays();
    final var hours = duration.toHours() % 24;
    final var minutes = duration.toMinutes() % 60;
    final var seconds = duration.getSeconds() % 60;
    return Lang.translatable(
        "duration.format",
        "days", days,
        "hours", hours,
        "minutes", minutes,
        "seconds", seconds
    );
  }
}

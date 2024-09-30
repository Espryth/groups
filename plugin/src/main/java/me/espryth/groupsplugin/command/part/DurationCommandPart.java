package me.espryth.groupsplugin.command.part;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

/**
 * A {@link CommandPart} that parses a {@link Duration} from a string.
 */
public class DurationCommandPart implements PartFactory {

  private static final Pattern DURATION_PATTERN = Pattern.compile("(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?");

  @Override
  public CommandPart createPart(final String name, final List<? extends Annotation> list) {
    return new ArgumentPart() {

      @Override
      public List<Duration> parseValue(final CommandContext ctx, final ArgumentStack stack, final CommandPart part) throws ArgumentParseException {
        final var argument = stack.next();
        if (argument.isEmpty()) {
          return List.of(Duration.ZERO);
        }
        final var matcher = DURATION_PATTERN.matcher(argument);
        if (!matcher.matches()) {
          throw new ArgumentParseException("duration.invalid");
        }
        final var days = matcher.group(1);
        final var hours = matcher.group(2);
        final var minutes = matcher.group(3);
        final var seconds = matcher.group(4);
        var duration = Duration.ZERO;
        try {
          if (days != null) {
            duration = duration.plusDays(Long.parseLong(days));
          }
          if (hours != null) {
            duration = duration.plusHours(Long.parseLong(hours));
          }
          if (minutes != null) {
            duration = duration.plusMinutes(Long.parseLong(minutes));
          }
          if (seconds != null) {
            duration = duration.plusSeconds(Long.parseLong(seconds));
          }
          return List.of(duration);
        } catch (final NumberFormatException e) {
          throw new ArgumentParseException("duration.invalid");
        }
      }

      @Override
      public String getName() {
        return name;
      }
    };
  }
}

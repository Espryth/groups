package me.espryth.groupsplugin.lang.argument;

import java.util.Map;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * A translatable component with named arguments.
 */
public class TranslatableNamedArgumentsComponent extends TranslatableComponentWrapper {

  private final Map<String, Object> arguments;

  public TranslatableNamedArgumentsComponent(
      final @NotNull TranslatableComponent delegated,
      final @NotNull Map<String, Object> arguments
  ) {
    super(delegated);
    this.arguments = arguments;
  }

  /**
   * Get the named arguments.
   *
   * @return The named arguments.
   */
  public @NotNull Map<String, Object> namedArguments() {
    return this.arguments;
  }
}

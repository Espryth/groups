package me.espryth.groupsplugin.lang.argument;

import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * A translatable component that holds a tag resolver.
 */
public class TagResolverComponent extends TranslatableComponentWrapper {

  private final TagResolver namedArguments;

  public TagResolverComponent(
      final @NotNull TranslatableComponent delegated,
      final @NotNull TagResolver namedArguments
  ) {
    super(delegated);
    this.namedArguments = namedArguments;
  }

  /**
   * Get the tag resolver.
   *
   * @return The tag resolver.
   */
  public @NotNull TagResolver resolver() {
    return this.namedArguments;
  }
}

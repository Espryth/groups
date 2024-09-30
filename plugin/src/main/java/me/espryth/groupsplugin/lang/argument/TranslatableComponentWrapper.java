package me.espryth.groupsplugin.lang.argument;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A simple wrapper for {@link TranslatableComponent}.
 */
public abstract class TranslatableComponentWrapper implements TranslatableComponent {

  private final TranslatableComponent delegated;

  protected TranslatableComponentWrapper(final @NotNull TranslatableComponent delegated) {
    this.delegated = delegated;
  }

  @Override
  public @NotNull String key() {
    return delegated.key();
  }

  @Override
  public @NotNull TranslatableComponent key(@NotNull String key) {
    return delegated.key(key);
  }

  @Override
  public @NotNull List<Component> args() {
    return delegated.args();
  }

  @Override
  public @NotNull List<TranslationArgument> arguments() {
    return delegated.arguments();
  }

  @Override
  public @NotNull TranslatableComponent arguments(@NotNull ComponentLike @NotNull ... args) {
    return delegated.arguments(args);
  }

  @Override
  public @NotNull TranslatableComponent arguments(@NotNull List<? extends ComponentLike> args) {
    return delegated.arguments(args);
  }

  @Override
  public @Nullable String fallback() {
    return delegated.fallback();
  }

  @Override
  public @NotNull TranslatableComponent fallback(@Nullable String fallback) {
    return delegated.fallback(fallback);
  }

  @Override
  public @NotNull Builder toBuilder() {
    return delegated.toBuilder();
  }

  @Override
  public @Unmodifiable @NotNull List<Component> children() {
    return delegated.children();
  }

  @Override
  public @NotNull TranslatableComponent children(@NotNull List<? extends ComponentLike> children) {
    return delegated.children(children);
  }

  @Override
  public @NotNull Style style() {
    return delegated.style();
  }

  @Override
  public @NotNull TranslatableComponent style(@NotNull Style style) {
    return delegated.style(style);
  }

}

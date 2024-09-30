package me.espryth.groupsplugin.lang;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import me.espryth.groupsplugin.lang.argument.TagResolverComponent;
import me.espryth.groupsplugin.lang.argument.TranslatableNamedArgumentsComponent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for translating messages and sending them to an audience.
 */
public final class Lang {

  private Lang() {
  }

  /**
   * Sends a message to the audience.
   *
   * @param audience The audience to send the message to.
   * @param key The key of the message to send.
   * @param namedArguments The named arguments to use in the message.
   */
  public static void send(final @NotNull Audience audience, final @NotNull String key, final @NotNull Object... namedArguments) {
    audience.sendMessage(translatable(key, namedArguments));
  }

  /**
   * Sends a message to the audience.
   *
   * @param audience The audience to send the message to.
   * @param key The key of the message to send.
   * @param resolvers The tag resolvers to use in the message.
   */
  public static void send(final @NotNull Audience audience, final @NotNull String key, TagResolver... resolvers) {
    audience.sendMessage(translatable(key, resolvers));
  }

  /**
   * Gets a translatable component.
   *
   * @param key The key of the translatable component.
   * @return The translatable component.
   */
  public static @NotNull TranslatableComponent translatable(final @NotNull String key) {
    return Component.translatable(key);
  }

  /**
   * Gets a translatable component.
   *
   * @param key The key of the translatable component.
   * @param args The arguments to use in the translatable component
   * @return The translatable component.
   */
  public static @NotNull TranslatableComponent translatable(final @NotNull String key, final @NotNull ComponentLike... args) {
    return Component.translatable(key, args);
  }

  /**
   * Gets a translatable component.
   *
   * @param key The key of the translatable component.
   * @param args The arguments to use in the translatable component
   * @return The translatable component.
   */
  public static @NotNull TranslatableComponent translatable(final @NotNull String key, final @NotNull Component... args) {
    return Component.translatable(key, args);
  }

  /**
   * Gets a translatable component.
   *
   * @param key The key of the translatable component.
   * @param namedArguments The named arguments to use in the translatable component.
   * @return The translatable component.
   */
  public static @NotNull TranslatableComponent translatable(final @NotNull String key, final @NotNull Object... namedArguments) {
    return translatable(key, of(namedArguments));
  }

  /**
   * Gets a translatable component.
   *
   * @param key The key of the translatable component.
   * @param resolvers The tag resolvers to use in the translatable component.
   * @return The translatable component.
   */
  public static TranslatableComponent translatable(final @NotNull String key, final @NotNull TagResolver... resolvers) {
    return new TagResolverComponent(Component.translatable(key), TagResolver.resolver(resolvers));
  }

  /**
   * Gets a translatable component.
   *
   * @param key The key of the translatable component.
   * @param namedArguments The named arguments
   * @return The translatable component.
   */
  public static TranslatableComponent translatable(final @NotNull String key, final @NotNull Map<String, Object> namedArguments) {
    return new TranslatableNamedArgumentsComponent(Component.translatable(key), namedArguments);
  }

  /**
   * Translates a translatable component.
   *
   * @param player The player to translate the component for.
   * @param key The key of the translatable component.
   * @return The translated component.
   */
  public static Component translate(final @Nullable Audience player, final @NotNull TranslatableComponent key) {
    final var translation = GlobalTranslator.translator()
        .translate(key, player == null ? Locale.ENGLISH : player.get(Identity.LOCALE).orElse(Locale.ENGLISH));
    if (translation == null) {
      throw new IllegalArgumentException("No translation found for key " + key.key());
    }
    return translation;
  }

  /**
   * Translates a translatable component.
   *
   * @param key The key of the translatable component.
   * @return The translated component.
   */
  public static Component translate(final @NotNull TranslatableComponent key) {
    return translate(null, key);
  }

  /**
   * Creates a map from the given named arguments.
   *
   * @param values The named arguments.
   * @return The map.
   */
  private static Map<String, Object> of(final @NotNull Object... values) {
    if (values.length % 2 != 0) {
      throw new IllegalArgumentException("Values must be in pairs");
    }
    final var map = new HashMap<String, Object>();
    for (int i = 0; i < values.length; i++) {
      if (i % 2 == 0) {
        final var key = values[i];
        if (!(key instanceof String)) {
          throw new IllegalArgumentException("Key '" + key + "' must be a string");
        }
        map.put((String) key, values[i + 1]);
      }
    }
    return map;
  }
}

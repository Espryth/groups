package me.espryth.groupsplugin.lang.translator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import me.espryth.groupsplugin.config.BukkitConfigFile;
import me.espryth.groupsplugin.lang.argument.TagResolverComponent;
import me.espryth.groupsplugin.lang.argument.TranslatableNamedArgumentsComponent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.Translator;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A translator that uses a {@link BukkitConfigFile} to translate components.
 *
 * @param name The name of the translator
 * @param files The files to use for translation
 */
public record ConfigFileTranslator(@NotNull Key name, @NotNull Map<String, BukkitConfigFile> files) implements Translator {

  @Override
  public @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale locale) {
    return null;
  }

  @Override
  public @Nullable Component translate(final @NotNull TranslatableComponent component, final @NotNull Locale locale) {
    final var file = getFile(locale);
    if (component instanceof TranslatableNamedArgumentsComponent namedArgumentsComponent) {
      final var key = namedArgumentsComponent.key();
      final var namedArguments = namedArgumentsComponent.namedArguments();
      return file.getComponent(key, from(namedArguments, locale));
    } else if (component instanceof TagResolverComponent tagResolverComponent) {
      final var key = tagResolverComponent.key();
      final var resolver = tagResolverComponent.resolver();
      return file.getComponent(key, resolver);
    }
    return file.getComponent(component.key());
  }

  /**
   * Gets the file for the specified locale.
   *
   * @param locale The locale to get the file for.
   * @return The file for the specified locale.
   */
  public @NotNull BukkitConfigFile getFile(final @NotNull Locale locale) {
    var file = files.get(locale.getLanguage());
    if (file == null) {
      file = Objects.requireNonNull(files.get(LangResourceProvider.DEFAULT_LOCALE), "Default locale file not found");
    }
    return file;
  }

  private @NotNull TagResolver from(final @NotNull Map<String, Object> namedArguments, final @NotNull Locale locale) {
    final var resolvers = new ArrayList<TagResolver>();
    for (final var entry : namedArguments.entrySet()) {
      @Subst("") final var key = entry.getKey();
      final var value = entry.getValue();
      if (value instanceof Tag tag) {
        resolvers.add(TagResolver.resolver(key, tag));
      } else {
        resolvers.add(TagResolver.resolver(key, Tag.selfClosingInserting(resolve(value, locale))));
      }
    }
    return TagResolver.resolver(resolvers);
  }

  private @NotNull Component resolve(final @NotNull Object object, final @NotNull Locale locale) {
    switch (object) {
      case TranslatableComponent translatable -> {
        return Objects.requireNonNullElse(translate(translatable, locale), Component.text(translatable.key()));
      }
      case Component component -> {
        return component;
      }
      case String s -> {
        return Component.text(s);
      }
      default -> {
        return Component.text(object.toString());
      }
    }
  }
}

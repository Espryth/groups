package me.espryth.groupsplugin.lang;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import me.espryth.groupsplugin.config.BukkitConfigFile;
import me.espryth.groupsplugin.lang.translator.ConfigFileTranslator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LangTest {

  @Test
  void testMultiLang() throws URISyntaxException {
    final var classLoader = getClass().getClassLoader();
    final var englishFile = BukkitConfigFile.create(Path.of(classLoader.getResource("en.yml").toURI()));
    englishFile.load();;
    final var germanFile = BukkitConfigFile.create(Path.of(classLoader.getResource("de.yml").toURI()));
    germanFile.load();
    final var translator = new ConfigFileTranslator(
        Key.key("test"),
        Map.of(
            "en", englishFile,
            "de", germanFile
        )
    );
    GlobalTranslator.translator().addSource(translator);
    final var translatable = Component.translatable("example");
    final var englishAudience = new AudienceImpl(Locale.ENGLISH);
    final var germanAudience = new AudienceImpl(Locale.GERMAN);
    final var emptyAudience = new AudienceImpl(null);
    assertEquals("Hello world", extractContent(Lang.translate(englishAudience, translatable)));
    assertEquals("Hallo welt", extractContent(Lang.translate(germanAudience, translatable)));
    assertEquals("Hello world", extractContent(Lang.translate(emptyAudience, translatable))); // default locale
  }

  private String extractContent(Component component) {
    return ((TextComponent) component).content();
  }

  private record AudienceImpl(@Nullable Locale locale) implements Audience {

      @Override
      public @NotNull Pointers pointers() {
        return Pointers.builder()
            .withStatic(Identity.LOCALE, locale)
            .build();
      }
    }
}

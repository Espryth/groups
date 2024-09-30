package me.espryth.groupsplugin.command.translator;

import java.util.function.Function;
import me.espryth.groupsplugin.lang.Lang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.bukkit.BukkitCommonConstants;
import team.unnamed.commandflow.translator.TranslationProvider;
import team.unnamed.commandflow.translator.Translator;

/**
 * Translates the component to the language of the sender.
 */
public class LangCommandTranslator implements Translator {

  @Override
  public Component translate(final Component component, final Namespace namespace) {
    final var sender = namespace.getObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE);
    if (component == null) {
      return Component.empty();
    } else if (component instanceof TranslatableComponent translatableComponent) {
      return Lang.translate(sender, translatableComponent);
    } else {
      return Lang.translate(sender, Lang.translatable(MiniMessage.miniMessage().serialize(component)));
    }
  }

  @Override
  public void setProvider(final TranslationProvider translationProvider) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void setConverterFunction(final Function<String, Component> function) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}

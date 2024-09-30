package me.espryth.groupsplugin;

import com.google.inject.AbstractModule;
import me.espryth.groupsplugin.group.GroupModule;
import me.espryth.groupsplugin.lang.translator.LangResourceProvider;
import me.espryth.groupsplugin.repository.RepositoryModule;
import me.espryth.groupsplugin.scoreboard.ScoreboardModule;
import me.espryth.groupsplugin.tab.TabModule;
import me.espryth.groupsplugin.user.UserModule;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

/**
 * This class is responsible for binding the plugin and its dependencies.
 */
public class PluginModule extends AbstractModule {

  private final Plugin plugin;

  public PluginModule(final Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(Logger.class).toInstance(plugin.getSLF4JLogger());
    LangResourceProvider.load(plugin);
    install(new RepositoryModule());
    install(new GroupModule());
    install(new UserModule());
    install(new TabModule());
    install(new ScoreboardModule());
  }
}

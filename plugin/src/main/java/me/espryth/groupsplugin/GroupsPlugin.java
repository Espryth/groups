package me.espryth.groupsplugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.time.Duration;
import me.espryth.groupsplugin.command.GroupCommand;
import me.espryth.groupsplugin.command.part.DurationCommandPart;
import me.espryth.groupsplugin.command.part.GroupCommandPart;
import me.espryth.groupsplugin.command.translator.LangCommandTranslator;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.listener.InjectPermissibleWrapperListener;
import me.espryth.groupsplugin.listener.ChatListener;
import me.espryth.groupsplugin.listener.PlayerJoinQuitListener;
import me.espryth.groupsplugin.scoreboard.listener.ScoreboardListener;
import me.espryth.groupsplugin.tab.listener.TabListeners;
import me.espryth.groupsplugin.user.UserExpireGroups;
import me.espryth.groupsplugin.user.listener.UserLoadListeners;
import me.espryth.groupsplugin.user.listener.UserUpdateEventDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import team.unnamed.commandflow.annotated.AnnotatedCommandTreeBuilder;
import team.unnamed.commandflow.annotated.part.PartInjector;
import team.unnamed.commandflow.annotated.part.defaults.DefaultsModule;
import team.unnamed.commandflow.bukkit.BukkitMapCommandManager;
import team.unnamed.commandflow.bukkit.factory.BukkitModule;

/**
 * Main class of the plugin.
 */
public class GroupsPlugin extends JavaPlugin {

  private Injector injector;

  @Override
  public void onLoad() {
    injector = Guice.createInjector(new PluginModule(this));
  }

  @Override
  public void onEnable() {
    final var partInjector = PartInjector.create();
    partInjector.install(new DefaultsModule());
    partInjector.install(new BukkitModule());
    partInjector.bindFactory(Duration.class, this.injector.getInstance(DurationCommandPart.class));
    partInjector.bindFactory(Group.class, this.injector.getInstance(GroupCommandPart.class));
    final var commandTree = AnnotatedCommandTreeBuilder.create(partInjector, (type, commandClass) -> this.injector.getInstance(type));
    final var commandManager = new BukkitMapCommandManager(this);
    commandManager.setTranslator(new LangCommandTranslator());
    commandManager.registerCommands(commandTree.fromClass(this.injector.getInstance(GroupCommand.class)));
    registerListeners(
        InjectPermissibleWrapperListener.class,
        TabListeners.class,
        UserLoadListeners.class,
        UserUpdateEventDispatcher.class,
        ChatListener.class,
        PlayerJoinQuitListener.class,
        ScoreboardListener.class
    );
    Bukkit.getScheduler().runTaskTimerAsynchronously(this, this.injector.getInstance(UserExpireGroups.class), 0, 20);
  }

  @SafeVarargs
  private void registerListeners(final @NotNull Class<? extends Listener>... listeners) {
    final var pluginManager = Bukkit.getPluginManager();
    for (final var listener : listeners) {
      pluginManager.registerEvents(this.injector.getInstance(listener), this);
    }
  }
}

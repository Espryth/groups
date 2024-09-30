package me.espryth.groupsplugin.scoreboard;

import com.google.inject.AbstractModule;

/**
 * This class is responsible for binding the ScoreboardService.
 */
public class ScoreboardModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ScoreboardService.class);
  }
}

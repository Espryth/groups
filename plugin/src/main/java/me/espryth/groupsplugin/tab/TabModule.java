package me.espryth.groupsplugin.tab;

import com.google.inject.AbstractModule;

/**
 * Tab module.
 */
public class TabModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TabService.class);
  }
}

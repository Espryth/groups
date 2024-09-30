package me.espryth.groupsplugin.group;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.util.concurrent.Executor;
import me.espryth.groupsplugin.group.permission.Permission;
import me.espryth.groupsplugin.repository.Repository;
import me.espryth.groupsplugin.repository.mapping.table.TableFactory;
import org.jdbi.v3.core.Jdbi;

/**
 * Guice module for group package.
 */
public class GroupModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(GroupService.class);
  }

  @Provides
  @Singleton
  Repository<Group> provideGroupRepository(final Jdbi connection, final Executor asyncExecutor) {
    return new Repository<>(connection, TableFactory.create(Group.class), Group.CODEC, asyncExecutor);
  }

  @Provides
  @Singleton
  Repository<Permission> providePermissionRepository(final Jdbi connection, final Executor asyncExecutor) {
    return new Repository<>(connection, TableFactory.create(Permission.class), Permission.CODEC, asyncExecutor);
  }
}

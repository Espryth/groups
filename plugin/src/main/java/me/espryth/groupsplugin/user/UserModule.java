package me.espryth.groupsplugin.user;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.util.concurrent.Executor;
import me.espryth.groupsplugin.repository.Repository;
import me.espryth.groupsplugin.repository.mapping.table.TableFactory;
import org.jdbi.v3.core.Jdbi;

/**
 * User module.
 */
public class UserModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UserService.class);
    bind(UserExpireGroups.class);
  }

  @Provides
  @Singleton
  Repository<UserGroup> provideUserGroupRepository(final Jdbi connection, final Executor asyncExecutor) {
    return new Repository<>(connection, TableFactory.create(UserGroup.class), UserGroup.CODEC, asyncExecutor);
  }
}

package me.espryth.groupsplugin.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;

/**
 * This class represents a task that removes expired groups from online users.
 */
@Singleton
public final class UserExpireGroups implements Runnable {

  private final UserService userService;
  private final Logger logger;

  @Inject
  UserExpireGroups(
      final UserService userService,
      final Logger logger
  ) {
    this.userService = userService;
    this.logger = logger;
  }

  /**
   * Checks for expired groups in online users.
   */
  @Override
  public void run() {
    for (final var user : this.userService.getOnlineUsers()) {
      for (final var group : user.groups()) {
        if (group.isExpired()) {
          this.userService.removeGroupFromUser(user.id(), group.groupId()).whenComplete((result, throwable) -> {
            if (throwable != null) {
              logger.error("An error occurred while removing the group from the user", throwable);
              return;
            }
            logger.info("Removing group {} from user {} because expired ({})", group.groupId(), user.id(), result);
          });
        }
      }
    }
  }
}

package me.espryth.groupsplugin.user;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import me.espryth.groupsplugin.group.Group;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserGroupExpireTest {

  @Test
  void userGroupExpires() {
    final var group = Group.create("test");
    final var user = new User(UUID.randomUUID(), List.of());
    final var userGroup = new UserGroup(user, group, Duration.ofMinutes(10));
    final var after = Instant.now().plus(Duration.ofMinutes(10).plusSeconds(1));
    assertFalse(userGroup.isExpired());
    assertTrue(userGroup.isExpired(after));
  }

}

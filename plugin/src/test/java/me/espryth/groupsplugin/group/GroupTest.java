package me.espryth.groupsplugin.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GroupTest {

  @Test
  void testGroupSort() {
    final var group = Group.create("First");
    final var group2 = Group.create("Second");
    final var group3 = Group.create("Third");
    group.weight(10);
    group2.weight(5);
    group3.weight(0);
    final var unordered = Arrays.asList(group3, group, group2);
    final var ordered = new ArrayList<>(unordered);
    Collections.sort(ordered);
    assertNotEquals(ordered, unordered);
    assertEquals(ordered, Arrays.asList(group, group2, group3));
  }
}

package me.espryth.groupsplugin.repository.mapping.table;

import me.espryth.groupsplugin.repository.mapping.annotation.Entity;
import me.espryth.groupsplugin.repository.mapping.annotation.ForeignKey;
import me.espryth.groupsplugin.repository.mapping.annotation.ForeignKeyAction;
import me.espryth.groupsplugin.repository.mapping.column.Column;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignAction;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignTrigger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {

  @Test
  void testTableCreation() {
    assertDoesNotThrow(() -> TableFactory.create(ModelOne.class));
    assertThrows(IllegalArgumentException.class, () -> TableFactory.create(ModelTwo.class));
  }

  @Test
  void testTableAnnotations() {
    final var table = TableFactory.create(ModelOne.class);
    final var columns = table.columns();
    Column reference = null;
    for (final var column : columns) {
      if (column.name().equals("reference")) {
        reference = column;
        break;
      }
    }
    assertNotNull(reference);
    assertNotNull(reference.foreignKey());
    assertEquals("example", reference.foreignKey().table());
    assertEquals("reference", reference.foreignKey().column());
    assertEquals(ForeignAction.NULL, reference.foreignKey().actions().get(ForeignTrigger.DELETE));
  }

  @Entity(table = "model_one")
  private static class ModelOne {

    private final String name;

    @ForeignKey(
        table = "example",
        column = "reference",
        actions = {
            @ForeignKeyAction(
                trigger = ForeignTrigger.DELETE,
                action = ForeignAction.NULL
            ),
        }
    )
    private final String reference;
    private int age;

    public ModelOne(String name, final String reference, int age) {
      this.name = name;
      this.reference = reference;
      this.age = age;
    }

    public String name() {
      return name;
    }

    public String getReference() {
      return reference;
    }

    public int age() {
      return age;
    }

    public void age(int age) {
      this.age = age;
    }
  }

  private static class ModelTwo {

    private final String name;
    private int age;

    public ModelTwo(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String name() {
      return name;
    }

    public int age() {
      return age;
    }

    public void age(int age) {
      this.age = age;
    }
  }
}

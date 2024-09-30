package me.espryth.groupsplugin.repository.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignAction;
import me.espryth.groupsplugin.repository.mapping.column.foreign.ForeignTrigger;

/**
 * The purpose of this annotation is to define the action of a foreign key.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ForeignKeyAction {

  /**
   * The trigger of the foreign key.
   *
   * @return The trigger of the foreign key.
   */
  ForeignTrigger trigger();

  /**
   * The action of the foreign key.
   *
   * @return The action of the foreign key.
   */
  ForeignAction action();
}

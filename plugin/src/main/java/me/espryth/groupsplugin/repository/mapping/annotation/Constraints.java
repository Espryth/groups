package me.espryth.groupsplugin.repository.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.espryth.groupsplugin.repository.mapping.column.Constraint;

/**
 * The purpose of this annotation is to define the constraints of a column in a table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Constraints {

  /**
   * The constraints of a column in a table.
   *
   * @return An array of {@link Constraint} representing the constraints of a column in a table.
   */
  Constraint[] value() default {};

}

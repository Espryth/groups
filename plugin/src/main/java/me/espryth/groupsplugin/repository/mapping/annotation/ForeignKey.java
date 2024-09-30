package me.espryth.groupsplugin.repository.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The purpose of this annotation is to define a foreign key in a table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {

  /**
   * The table name that the foreign key references.
   *
   * @return The table name that the foreign key references.
   */
  String table();

  /**
   * The column name that the foreign key references.
   *
   * @return The column name that the foreign key references.
   */
  String column();

  /**
   * The actions that will be executed when the foreign key is updated or deleted.
   *
   * @return The actions that will be executed when the foreign key is updated or deleted.
   */
  ForeignKeyAction[] actions() default {};
}

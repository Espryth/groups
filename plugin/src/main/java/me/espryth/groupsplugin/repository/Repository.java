package me.espryth.groupsplugin.repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import me.espryth.groupsplugin.repository.mapping.codec.Codec;
import me.espryth.groupsplugin.repository.mapping.table.Table;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

/**
 * A repository is a class that provides an interface to interact with a database.
 *
 * @param <M> The model type.
 */
public final class Repository<M> {

  private final Jdbi connection;
  private final Table table;
  private final Codec<M> codec;
  private final Executor executor;

  /**
   * Creates a new repository.
   *
   * @param connection The database connection.
   * @param table The model table.
   * @param codec The model codec.
   * @param executor The executor to run the queries.
   */
  public Repository(
      final @NotNull Jdbi connection,
      final @NotNull Table table,
      final @NotNull Codec<M> codec,
      final @NotNull Executor executor
  ) {
    this.connection = connection;
    this.table = table;
    this.codec = codec;
    this.executor = executor;
    this.connection.withHandle(handle ->
        handle.createUpdate("CREATE TABLE IF NOT EXISTS <TABLE> (<COLUMNS>)")
            .define("TABLE", table.name())
            .define("COLUMNS", table.toSql())
            .execute()
    );
  }

  /**
   * Finds a model by a column.
   *
   * @param column The column name.
   * @param value The column value.
   * @return A future with the model list.
   */
  public @NotNull CompletableFuture<List<M>> find(final @NotNull String column, final @NotNull Object value) {
    return CompletableFuture.supplyAsync(() ->
      this.connection.withHandle(handle ->
          handle.createQuery("SELECT * FROM <TABLE> WHERE <COLUMN> = :value")
              .define("TABLE", table.name())
              .define("COLUMN", column)
              .bind("value", value)
              .map((rs, ctx) -> codec.decode(rs))
              .list()
      ), this.executor
    );
  }

  /**
   * Finds all models.
   *
   * @return A future with the model list.
   */
  public @NotNull CompletableFuture<Collection<M>> findAll() {
    return CompletableFuture.supplyAsync(() ->
      this.connection.withHandle(handle ->
          handle.createQuery("SELECT * FROM <TABLE>")
              .define("TABLE", table.name())
              .map((rs, ctx) -> codec.decode(rs))
              .list()
      ), this.executor
    );
  }

  /**
   * Saves a model.
   *
   * @param model The model to save.
   * @return A future that completes when the model is saved.
   */
  public @NotNull CompletableFuture<Void> save(final @NotNull M model) {
    return CompletableFuture.runAsync(() ->
      this.connection.useHandle(handle ->
          handle.createUpdate("REPLACE INTO <TABLE> (<COLUMNS>) VALUES (<VALUES>)")
              .define("TABLE", table.name())
              .define("COLUMNS", table.columnNames())
              .define("VALUES", table.valueNames())
              .bindMap(codec.encode(model))
              .execute()
      ), this.executor
    );
  }

  /**
   * Deletes a model by a column.
   *
   * @param column The column name.
   * @param value The column value.
   * @return A future that completes when the model is deleted.
   */
  public @NotNull CompletableFuture<Void> delete(final @NotNull String column, final @NotNull Object value) {
    return CompletableFuture.runAsync(() ->
      this.connection.useHandle(handle ->
          handle.createUpdate("DELETE FROM <TABLE> WHERE <COLUMN> = :value")
              .define("TABLE", table.name())
              .define("COLUMN", column)
              .bind("value", value)
              .execute()
      ), this.executor
    );
  }
}

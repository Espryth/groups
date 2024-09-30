package me.espryth.groupsplugin.repository.mapping.codec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to encode and decode models.
 *
 * @param <M> the model type.
 */
public interface Codec<M> {

  /**
   * Encodes the model to a map.
   *
   * @param model The model to encode.
   * @return The map with the model data.
   */
  @NotNull Map<String, Object> encode(final @NotNull M model);

  /**
   * Decodes the result set to a model.
   *
   * @param resultSet The result set to decode.
   * @return The model with the result set data.
   * @throws SQLException If an error occurs while decoding the result set.
   */
  @NotNull M decode(final @NotNull ResultSet resultSet) throws SQLException;

}

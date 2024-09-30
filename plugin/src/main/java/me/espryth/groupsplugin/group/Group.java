package me.espryth.groupsplugin.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import me.espryth.groupsplugin.repository.mapping.annotation.Constraints;
import me.espryth.groupsplugin.repository.mapping.annotation.Entity;
import me.espryth.groupsplugin.repository.mapping.codec.Codec;
import me.espryth.groupsplugin.repository.mapping.column.Constraint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * The group model.
 */
@Entity(table = "groups")
public final class Group implements Comparable<Group> {

  public static final Codec<Group> CODEC = new GroupCodec();

  @Constraints(Constraint.PRIMARY)
  private final UUID id;

  @Constraints({Constraint.UNIQUE, Constraint.NOT_NULL})
  private String name;

  private String prefix;

  private String suffix;

  private int weight;

  private Group(
      final @NotNull UUID id,
      final @NotNull String name,
      final @NotNull String prefix,
      final @NotNull String suffix,
      final int weight
  ) {
    this.id = id;
    this.name = name;
    this.prefix = prefix;
    this.suffix = suffix;
    this.weight = weight;
  }

  /**
   * Create a new group.
   *
   * @param name The group name.
   * @return The group.
   */
  public static Group create(final @NotNull String name) {
    return new Group(UUID.randomUUID(), name, "", "", 0);
  }

  public @NotNull UUID id() {
    return id;
  }

  public @NotNull String name() {
    return name;
  }

  public void name(final @NotNull String name) {
    this.name = name;
  }

  public @NotNull Component prefix() {
    return MiniMessage.miniMessage().deserialize(prefix);
  }

  public void prefix(final @NotNull String prefix) {
    this.prefix = prefix;
  }

  public @NotNull Component suffix() {
    return MiniMessage.miniMessage().deserialize(suffix);
  }

  public void suffix(final @NotNull String suffix) {
    this.suffix = suffix;
  }

  public int weight() {
    return weight;
  }

  public void weight(final int weight) {
    this.weight = weight;
  }

  @Override
  public int compareTo(final @NotNull Group o) {
    return Integer.compare(o.weight, weight);
  }

  /**
   * Create a tag resolver for this group.
   *
   * @return The tag resolver.
   */
  public @NotNull TagResolver tagResolver() {
    return TagResolver.builder()
      .resolver(TagResolver.resolver("id", Tag.selfClosingInserting(Component.text(this.id().toString()))))
      .resolver(TagResolver.resolver("name", Tag.selfClosingInserting(Component.text(this.name()))))
      .resolver(TagResolver.resolver("prefix", Tag.selfClosingInserting(this.prefix())))
      .resolver(TagResolver.resolver("suffix", Tag.selfClosingInserting(this.suffix())))
      .resolver(TagResolver.resolver("weight", Tag.selfClosingInserting(Component.text(String.valueOf(this.weight())))))
      .build();
  }

  private static class GroupCodec implements Codec<Group> {

    @Override
    public @NotNull Map<String, Object> encode(final @NotNull Group model) {
      return Map.of(
        "id", model.id.toString(),
        "name", model.name,
        "prefix", model.prefix,
        "suffix", model.suffix,
        "weight", model.weight
      );
    }

    @Override
    public @NotNull Group decode(final @NotNull ResultSet resultSet) throws SQLException {
      return new Group(
        UUID.fromString(resultSet.getString("id")),
        resultSet.getString("name"),
        resultSet.getString("prefix"),
        resultSet.getString("suffix"),
        resultSet.getInt("weight")
      );
    }
  }
}

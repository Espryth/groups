package me.espryth.groupsplugin.command.part;

import com.google.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.List;
import me.espryth.groupsplugin.group.Group;
import me.espryth.groupsplugin.group.GroupService;
import me.espryth.groupsplugin.lang.Lang;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

/**
 * A {@link CommandPart} that parses a {@link Group} from a {@link String}.
 */
public class GroupCommandPart implements PartFactory {

  private @Inject GroupService groupService;

  @Override
  public CommandPart createPart(final String name, final List<? extends Annotation> list) {
    return new ArgumentPart() {

      @Override
      public List<Group> parseValue(final CommandContext ctx, final ArgumentStack stack, final CommandPart part) throws ArgumentParseException {
        final var argument = stack.next().toLowerCase();
        final var group = groupService.getByName(argument);
        if (group == null) {
          throw new ArgumentParseException(Lang.translatable("group.not-found", "group", argument));
        }
        return List.of(group);
      }

      @Override
      public List<String> getSuggestions(final CommandContext commandContext, final ArgumentStack stack) {
        return groupService.getGroups()
          .stream()
          .map(Group::name)
          .toList();
      }

      @Override
      public String getName() {
        return name;
      }
    };
  }
}

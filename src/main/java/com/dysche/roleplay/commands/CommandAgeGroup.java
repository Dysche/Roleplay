package com.dysche.roleplay.commands;

import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.command.CommandResult;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.command.CommandSpec;
import org.cantaloupe.command.CommandSpec.ErrorType;
import org.cantaloupe.command.ICommandExecutor;
import org.cantaloupe.command.args.CommandContext;
import org.cantaloupe.command.args.GeneralArguments;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;

import com.dysche.roleplay.Constants;
import com.dysche.roleplay.Main;
import com.dysche.roleplay.player.RoleplayWrapper;

public class CommandAgeGroup {
    public static void register(Main main) {
        CommandSpec spec = CommandSpec.builder()
                .description(Text.of("This command is used to change the age group of your character."))
                .usage(Text.of(Constants.USAGE_PREFIX + " /agegroup|ag <child/teen/youngadult/adult/elder>"))
                .error(ErrorType.NOT_ENOUGH_ARGUMENTS, Text.fromLegacy(Constants.COMMAND_PREFIX + " Invalid arguments."))
                .permission("roleplay.commands.agegroup")
                .arguments(GeneralArguments.string("group"))
                .executor(new ICommandExecutor() {
                    @Override
                    public CommandResult execute(CommandSource src, CommandContext args) {
                        Optional<String> groupOpt = args.<String>getOne("group");

                        if (groupOpt.isPresent()) {
                            Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromCommandSource(src);

                            if (playerOpt.isPresent()) {
                                Player player = playerOpt.get();
                                RoleplayWrapper wrapper = player.<RoleplayWrapper>getWrapper(RoleplayWrapper.class).get();

                                switch (groupOpt.get().toLowerCase()) {
                                    case "child":
                                    case "c":
                                        wrapper.setAgeGroup(GroupManager.getGroup("child").get());
                                        wrapper.updateAgeGroup();

                                        src.sendMessage(Text.fromLegacy(Constants.COMMAND_PREFIX + " You set your age group to &bChild&r."));

                                        break;
                                    case "teen":
                                    case "t":
                                        wrapper.setAgeGroup(GroupManager.getGroup("teen").get());
                                        wrapper.updateAgeGroup();

                                        src.sendMessage(Text.fromLegacy(Constants.COMMAND_PREFIX + " You set your age group to &9Teen&r."));

                                        break;
                                    case "youngadult":
                                    case "ya":
                                        wrapper.setAgeGroup(GroupManager.getGroup("youngadult").get());
                                        wrapper.updateAgeGroup();

                                        src.sendMessage(Text.fromLegacy(Constants.COMMAND_PREFIX + " You set your age group to &dYoung Adult&r."));

                                        break;
                                    case "adult":
                                    case "a":
                                        wrapper.setAgeGroup(GroupManager.getGroup("adult").get());
                                        wrapper.updateAgeGroup();

                                        src.sendMessage(Text.fromLegacy(Constants.COMMAND_PREFIX + " You set your age group to &aAdult&r."));

                                        break;
                                    case "elder":
                                    case "e":
                                        wrapper.setAgeGroup(GroupManager.getGroup("elder").get());
                                        wrapper.updateAgeGroup();

                                        src.sendMessage(Text.fromLegacy(Constants.COMMAND_PREFIX + " You set your age group to &cElder&r."));

                                        break;
                                    default:
                                        src.sendMessage(Text.fromLegacy(Constants.COMMAND_PREFIX + " This is not a valid age group."));

                                        return CommandResult.FAILURE;
                                }

                                return CommandResult.SUCCESS;
                            }
                        }

                        return CommandResult.FAILURE;
                    }
                }).build();

        main.registerCommand(spec, "agegroup", "ag");
    }
}

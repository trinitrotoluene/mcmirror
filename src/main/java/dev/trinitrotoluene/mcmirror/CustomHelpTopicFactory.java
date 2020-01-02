package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.commands.CommandDescriptor;
import dev.trinitrotoluene.mcmirror.util.commands.CommandService;
import org.bukkit.command.Command;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

public final class CustomHelpTopicFactory implements HelpTopicFactory<Command> {
    private CommandService _commands;

    public CustomHelpTopicFactory(CommandService commands) {
        this._commands = commands;
    }

    @Override
    public HelpTopic createTopic(Command command) {
        return MirrorHelpTopicFactory.Create(this._commands, command);
    }
}

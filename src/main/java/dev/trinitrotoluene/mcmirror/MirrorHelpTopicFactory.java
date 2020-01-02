package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.commands.CommandService;
import org.bukkit.command.Command;
import org.bukkit.help.IndexHelpTopic;

public class MirrorHelpTopicFactory {
    private MirrorHelpTopicFactory() {
    }

    public static IndexHelpTopic create(CommandService commands, Command command) {
        var moduleName = command.getName();

        var module = commands.getTopLevelModules()
                .stream()
                .filter(m -> m.hasLabel(moduleName))
                .findFirst()
                .orElseThrow();
    }
}

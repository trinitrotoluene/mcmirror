package dev.trinitrotoluene.mcmirror.util.commands;

import dev.trinitrotoluene.mcmirror.util.services.IServiceProvider;
import dev.trinitrotoluene.mcmirror.util.services.ServiceCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class CommandServiceBuilder {
    private Set<CommandModuleDescriptor> _topLevelModules;

    public CommandServiceBuilder() {
        this._topLevelModules = new HashSet<>();
    }

    public CommandServiceBuilder registerCommands(Class type) {
        registerCommands(type, null);
        return this;
    }

    private void registerCommands(Class type, CommandModuleDescriptor parentModule) {
        var group = (Group) type.getAnnotation(Group.class);
        String groupLabel = null;
        if (group != null)
            groupLabel = group.value();
        else
            groupLabel = "";

        var commands = Arrays.stream(type.getMethods())
                .filter(m -> m.isAnnotationPresent(Command.class))
                .collect(Collectors.toList());

        var commandSet = new HashSet<CommandDescriptor>();
        var currentModule = new CommandModuleDescriptor(groupLabel, type, commandSet);
        for (var command : commands) {
            commandSet.add(new CommandDescriptor(command, command.getAnnotation(Command.class).value(), currentModule));
        }

        for (var submodule : type.getDeclaredClasses()) {
            registerCommands(submodule, currentModule);
        }

        if (parentModule != null) {
            parentModule.addChild(currentModule);
        }
        else {
            this._topLevelModules.add(currentModule);
        }
    }

    public CommandService build(IServiceProvider services) {
        return new CommandService(this._topLevelModules, services);
    }
}

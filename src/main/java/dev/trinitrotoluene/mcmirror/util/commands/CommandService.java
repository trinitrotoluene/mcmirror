package dev.trinitrotoluene.mcmirror.util.commands;

import dev.trinitrotoluene.mcmirror.util.services.IServiceProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

public final class CommandService {
    private final IServiceProvider _services;
    private final Set<CommandModuleDescriptor> _topLevelModules;

    public CommandService(Set<CommandModuleDescriptor> topLevelModules, IServiceProvider services) {
        this._topLevelModules = topLevelModules;
        this._services = services;
    }

    public Boolean execute(String raw, ICommandContext context) throws InvocationTargetException, IllegalAccessException {
        var pathStack = parseString(raw);

        var command = search(pathStack);

        if (command.isPresent()) {
            return command.get().run(this._services, context);
        }

        return false;
    }

    public Optional<CommandDescriptor> search(String raw) {
        var pathStack = parseString(raw);

        return search(pathStack);
    }

    public Set<CommandModuleDescriptor> getTopLevelModules() {
        return this._topLevelModules;
    }

    private Stack<String> parseString(String raw) {
        var path = raw.split(" ");
        var pathStack = new Stack<String>();

        for (int i = path.length - 1; i >= 0; i--) {
            pathStack.push(path[i]);
        }

        return pathStack;
    }

    private Optional<CommandDescriptor> search(Stack<String> path) {
        var module = this._topLevelModules.stream()
                .filter(m -> m.hasLabel(path.peek()) || m.hasLabel(""))
                .findFirst();

        if (module.isPresent()) {
            if (!module.get().hasLabel(""))
                path.pop();

            return module.get().search(path);
        }

        return Optional.empty();
    }
}

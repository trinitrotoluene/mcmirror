package dev.trinitrotoluene.mcmirror.util.commands;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

public final class CommandModuleDescriptor {
    private final String _label;
    private final Class _type;
    private final Set<CommandDescriptor> _commands;

    private Set<CommandModuleDescriptor> _children;

    public CommandModuleDescriptor(String label, Class type, Set<CommandDescriptor> commands) {
        this._type = type;
        this._commands = commands;
        this._label = label;

        this._children = null;
    }

    public Class getType() {
        return this._type;
    }

    public boolean hasLabel(String label) {
        return this._label.equalsIgnoreCase(label);
    }

    public String getName() {
        return this._label;
    }

    public Set<CommandModuleDescriptor> getChildren() {
        return this._children;
    }

    public Set<CommandDescriptor> getCommands() {
        return this._commands;
    }

    public void addChild(CommandModuleDescriptor child) {
        if (this._children == null)
            this._children = new HashSet<>();

        this._children.add(child);
    }

    public Optional<CommandDescriptor> search(Stack<String> labels) {
        if (labels.size() == 0)
            return Optional.empty();

        var label = labels.pop();
        if (this._children == null) {
            return tryFind(label);
        }
        else {
            var module = this._children.stream()
                    .filter(m -> m.hasLabel(label))
                    .findFirst();

            if (module.isEmpty()) {
                return tryFind(label);
            }
            else {
                return module.get().search(labels);
            }
        }
    }

    private Optional<CommandDescriptor> tryFind(String label) {
        return this._commands.stream()
                .filter(c -> c.hasLabel(label))
                .findFirst();
    }
}

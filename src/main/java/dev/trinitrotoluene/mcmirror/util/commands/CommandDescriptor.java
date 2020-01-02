package dev.trinitrotoluene.mcmirror.util.commands;

import dev.trinitrotoluene.mcmirror.util.services.IServiceProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class CommandDescriptor {
    private final Method _callback;
    private final String _label;
    private final CommandModuleDescriptor _parent;

    public CommandDescriptor(Method callback, String label, CommandModuleDescriptor parent) {
        this._callback = callback;
        this._label = label;
        this._parent = parent;
    }

    public boolean hasLabel(String label) {
        return this._label.equalsIgnoreCase(label);
    }

    public String getName() {
        return this._label;
    }

    public String getRequiredPermission() {
        RequirePermission requirePermission = this._callback.getAnnotation(RequirePermission.class);
        if (requirePermission == null)
        {
            requirePermission = this._callback.getDeclaringClass().getAnnotation(RequirePermission.class);
            if (requirePermission == null) {
                return null;
            }
        }
        return requirePermission.value();
    }

    public Method getCallback() {
        return this._callback;
    }

    public CommandModuleDescriptor getModule() {
        return this._parent;
    }

    /** @noinspection unchecked*/
    public Boolean run(IServiceProvider services, ICommandContext context) throws InvocationTargetException, IllegalAccessException {
        CommandModule instance = services.getService(this._parent.getType());
        instance.setContext(context);

        return (Boolean) _callback.invoke(instance);
    }
}

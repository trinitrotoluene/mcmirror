package dev.trinitrotoluene.mcmirror.util.commands;

public abstract class CommandModule<T extends ICommandContext> {
    protected T Context;

    public void setContext(T context) {
        this.Context = context;
    }
}

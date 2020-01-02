package dev.trinitrotoluene.mcmirror.util.commands;

@Group
public class TestModuleA extends CommandModule<CommandContext> {
    @Command("foo")
    public Boolean Foo() {
        return true;
    }

    @Command("contextisnull")
    public Boolean Bar() {
        return this.Context == null;
    }
}

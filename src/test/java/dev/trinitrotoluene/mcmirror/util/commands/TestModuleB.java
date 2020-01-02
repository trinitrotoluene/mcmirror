package dev.trinitrotoluene.mcmirror.util.commands;

@Group("foo")
public class TestModuleB extends CommandModule<CommandContext> {
    @Group("baz")
    public class TestModuleC extends CommandModule<CommandContext> {
        @Command("bar")
        public Boolean bar() {
            return true;
        }
    }

    @Command("bar")
    public Boolean bar() {
        return true;
    }
}

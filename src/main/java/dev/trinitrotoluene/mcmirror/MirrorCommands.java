package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.commands.Command;
import dev.trinitrotoluene.mcmirror.util.commands.CommandModule;
import dev.trinitrotoluene.mcmirror.util.commands.Group;

public final class MirrorCommands extends CommandModule<BukkitCommandContext> {
    @Command("mirror")
    public Boolean index() {
        return false;
    }

    @Group("mirror")
    public final class MirrorSubCommands {
        @Command("enable")
        public Boolean enable() {
            return false;
        }
    }
}

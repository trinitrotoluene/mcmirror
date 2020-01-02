package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.commands.CommandDescriptor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

public final class MirrorHelpTopic extends HelpTopic {
    private final CommandDescriptor _descriptor;

    public MirrorHelpTopic(CommandDescriptor descriptor) {
        this._descriptor = descriptor;

        this.name = this._descriptor.getFullyQualifiedName();
        this.shortText = this._descriptor.getDescription();
        this.fullText = this._descriptor.getDescription();
    }

    @Override
    public boolean canSee(CommandSender commandSender) {
        if (this._descriptor.getRequiredPermission() != null) {
            return commandSender.hasPermission(this._descriptor.getRequiredPermission());
        }
        else {
            return true;
        }
    }
}

package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageMirror;
import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor {
    private final DiscordMessageMirror _discordMirror;
    private MinecraftMessageMirror _minecraftMirror;

    public CommandListener(DiscordMessageMirror discordMirror, MinecraftMessageMirror minecraftMirror) {
        this._discordMirror = discordMirror;
        this._minecraftMirror = minecraftMirror;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mirrorenable")) {
            if (this._discordMirror.getClient().isConnected())
                this._discordMirror.setEnabled(true);

            this._minecraftMirror.setEnabled(true);

            sender.sendMessage(ChatColor.GREEN + "Set mirroring to enabled!");
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrordisable")) {
            if (this._discordMirror.getClient().isConnected())
                this._discordMirror.setEnabled(false);

            this._minecraftMirror.setEnabled(false);

            sender.sendMessage(ChatColor.GREEN + "Set mirroring to disabled!");
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorkill")) {
            if (!this._discordMirror.getClient().isConnected()) {
                sender.sendMessage(ChatColor.RED + "Already disconnected.");
            }
            else {
                this._discordMirror.close();
                sender.sendMessage(ChatColor.GREEN + "Disconnected.");
            }

            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorconnect")) {
            if (this._discordMirror.getClient().isConnected()) {
                sender.sendMessage(ChatColor.RED + "Already connected or reconnecting.");
            }
            else {
                this._discordMirror.bindAndBroadcast();
                sender.sendMessage(ChatColor.GREEN + "Reconnecting!");
            }
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorstatus")) {
            sender.sendMessage(this._discordMirror.getStatus());
            return true;
        }
        return false;
    }
}

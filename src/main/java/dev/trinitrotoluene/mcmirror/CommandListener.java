package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageMirror;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

class CommandListener implements CommandExecutor {
    private final MirrorPlugin _plugin;

    CommandListener() {
        this._plugin = JavaPlugin.getPlugin(MirrorPlugin.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mirrorenable")) {
            if (this._plugin.getDiscordMessageMirror() != null)
                this._plugin.getDiscordMessageMirror().setEnabled(true);

            if (this._plugin.getMinecraftMessageMirror() != null)
                this._plugin.getMinecraftMessageMirror().setEnabled(true);

            sender.sendMessage(ChatColor.GREEN + "Set mirroring to enabled!");
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrordisable")) {
            if (this._plugin.getDiscordMessageMirror() != null)
                this._plugin.getDiscordMessageMirror().setEnabled(false);

            if (this._plugin.getMinecraftMessageMirror() != null)
                this._plugin.getMinecraftMessageMirror().setEnabled(false);

            sender.sendMessage(ChatColor.GREEN + "Set mirroring to disabled!");
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorkill")) {
            if (this._plugin.getDiscordMessageMirror() == null) {
                sender.sendMessage(ChatColor.RED + "Already disconnected.");
            }
            else {
                this._plugin.getDiscordMessageMirror().close();
                this._plugin.setDiscordMessageMirror(null);
                sender.sendMessage(ChatColor.GREEN + "Disconnected.");
            }

            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorconnect")) {
            if (this._plugin.getDiscordMessageMirror() != null) {
                sender.sendMessage(ChatColor.RED + "Already connected or reconnecting.");
            }
            else {
                this._plugin.setDiscordMessageMirror(new DiscordMessageMirror());
                this._plugin.getDiscordMessageMirror().bindAndBroadcast();
                sender.sendMessage(ChatColor.GREEN + "Reconnecting!");
            }
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorstatus")) {
            if (this._plugin.getDiscordMessageMirror() == null) {
                sender.sendMessage("The Discord mirror isn't initialised.");
            }
            else {
                sender.sendMessage(this._plugin.getDiscordMessageMirror().getStatus());
            }
            return true;
        }
        return false;
    }
}

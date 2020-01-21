package dev.trinitrotoluene.mcmirror.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import dev.trinitrotoluene.mcmirror.MirrorPlugin;
import dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageMirror;
import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("mirror")
@CommandPermission("mcmirror.admin")
public class AdminCommandModule extends BaseCommand {
    @Dependency
    public DiscordMessageMirror DiscordMirror;
    @Dependency
    public MinecraftMessageMirror MinecraftMirror;
    @Dependency
    public MirrorPlugin Plugin;

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("enable")
    public void onEnable(CommandSender sender) {
        this.DiscordMirror.setEnabled(true);
        this.MinecraftMirror.setEnabled(true);

        sender.sendMessage(ChatColor.GREEN + "Mirroring enabled.");
    }

    @Subcommand("disable")
    public void onDisable(CommandSender sender) {
        this.DiscordMirror.setEnabled(false);
        this.MinecraftMirror.setEnabled(false);

        sender.sendMessage(ChatColor.GREEN + "Mirroring disabled.");
    }

    @Subcommand("kill")
    public void onKill(CommandSender sender) {
        if (this.DiscordMirror.getClient() != null && !this.DiscordMirror.getClient().isConnected()) {
            sender.sendMessage(ChatColor.RED + "Already disconnected.");
        }
        else {
            this.DiscordMirror.close();
            sender.sendMessage(ChatColor.GREEN + "Disconnected.");
        }
    }

    @Subcommand("connect")
    public void onConnect(CommandSender sender) {
        if (this.DiscordMirror.getClient() != null) {
            sender.sendMessage(ChatColor.RED + "Already connected or reconnecting.");
        }
        else {
            this.DiscordMirror.bindAndBroadcast();
            sender.sendMessage(ChatColor.GREEN + "Reconnecting...");
        }
    }

    @Subcommand("status")
    public void onStatus(CommandSender sender) {
        sender.sendMessage(this.DiscordMirror.getStatus());
    }
}

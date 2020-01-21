package dev.trinitrotoluene.mcmirror;

import co.aikar.commands.BukkitCommandManager;
import dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageMirror;
import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import dev.trinitrotoluene.mcmirror.util.services.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class MirrorPlugin extends JavaPlugin {
    private IServiceProvider _services;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            this._services = new ServiceCollection()
                    .addSingleton(FileConfiguration.class, this.getConfig())
                    .addSingleton(Logger.class, this.getLogger())
                    .addSingleton(MinecraftMessageMirror.class)
                    .addSingleton(DiscordMessageMirror.class)
                    .addSingleton(WebhookProvider.class)
                    .addSingleton(MirrorPlugin.class, this)
                    .addSingleton(CommandListener.class)
                    .addSingleton(BukkitCommandManager.class, new BukkitCommandManager(this))
                    .build();
        }
        catch (MissingDependencyException e) {
            getLogger().severe("Failed to resolve a core dependency!");
        }
        catch (CircularDependencyException e) {
            getLogger().severe("A circular dependency was found while attempting to build the service provider.");
        }

        try {
            BukkitCommandManager manager = this._services.getRequiredService(BukkitCommandManager.class);
            manager.registerDependency(MinecraftMessageMirror.class, _services.getRequiredService(MinecraftMessageMirror.class));
            manager.registerDependency(DiscordMessageMirror.class, _services.getRequiredService(DiscordMessageMirror.class));

            MinecraftMessageMirror mc = this._services.getRequiredService(MinecraftMessageMirror.class);
            this.getServer().getPluginManager().registerEvents(mc, this);

            DiscordMessageMirror dc = this._services.getRequiredService(DiscordMessageMirror.class);
            dc.bindAndBroadcast();
        }
        catch (ServiceNotFoundException e) {
            getLogger().severe("Couldn't load required services!");
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        try {
            DiscordMessageMirror dc = this._services.getRequiredService(DiscordMessageMirror.class);
            dc.close();
            MinecraftMessageMirror mc = this._services.getRequiredService(MinecraftMessageMirror.class);
            mc.close();
            BukkitCommandManager cm = this._services.getRequiredService(BukkitCommandManager.class);
            cm.unregisterCommands();
        }
        catch (ServiceNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            this._services = null;
        }
    }
}

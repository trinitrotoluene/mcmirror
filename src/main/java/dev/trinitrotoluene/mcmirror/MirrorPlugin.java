package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageMirror;
import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import dev.trinitrotoluene.mcmirror.util.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class MirrorPlugin extends JavaPlugin {
    private IServiceProvider _services;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            this._services = new ServiceCollection()
                    .addSingleton(FileConfiguration.class, this.getConfig())
                    .addSingleton(MinecraftMessageMirror.class)
                    .addSingleton(DiscordMessageMirror.class)
                    .addSingleton(WebhookProvider.class)
                    .addSingleton(MirrorPlugin.class, this)
                    .addSingleton(CommandListener.class)
                    .addSingleton(PluginTabCompleter.class)
                    .build();
        }
        catch (MissingDependencyException e) {
            getLogger().severe("Failed to resolve a core dependency!");
        }
        catch (CircularDependencyException e) {
            getLogger().severe("A circular dependency was found while attempting to build the service provider.");
        }

        try {
            MinecraftMessageMirror mc = this._services.getRequiredService(MinecraftMessageMirror.class);
            this.getServer().getPluginManager().registerEvents(mc, this);

            DiscordMessageMirror dc = this._services.getRequiredService(DiscordMessageMirror.class);
            dc.bindAndBroadcast();
        }
        catch (ServiceNotFoundException e) {
            getLogger().severe("Couldn't load ServiceManager!");
        }

        registerCommands();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        try {
            MinecraftMessageMirror mc = this._services.getRequiredService(MinecraftMessageMirror.class);
            DiscordMessageMirror dc = this._services.getRequiredService(DiscordMessageMirror.class);

            mc.close();
            dc.close();
        }
        catch (ServiceNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        CommandListener listener = this._services.getService(CommandListener.class);
        PluginTabCompleter completer = this._services.getService(PluginTabCompleter.class);

        this.getCommand("mirror").setExecutor(listener);
        this.getCommand("mirror").setTabCompleter(completer);
    }
}

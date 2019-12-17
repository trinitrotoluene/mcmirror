package dev.trinitrotoluene.mcmirror;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PluginTabCompleter implements TabCompleter {
    private final Map<String, String> _commandPermnodeMap = new HashMap<>();

    public PluginTabCompleter() {
        this._commandPermnodeMap.put("enable", "mcmirror.enable");
        this._commandPermnodeMap.put("disable", "mcmirror.disable");
        this._commandPermnodeMap.put("disconnect", "mcmirror.disconnect");
        this._commandPermnodeMap.put("connect", "mcmirror.connect");
        this._commandPermnodeMap.put("status", "mcmirror.status");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var suggestions = new ArrayList<String>();

        for (var kvp : this._commandPermnodeMap.entrySet())
            if (sender.hasPermission(kvp.getValue()))
                suggestions.add(kvp.getKey());

        return suggestions;
    }
}

package dev.trinitrotoluene.mcmirror.util;

import org.bukkit.Bukkit;

import java.util.regex.Pattern;

public final class MinecraftVersion {
    private final Integer _major;
    private final Integer _minor;
    private final Integer _patch;

    private MinecraftVersion(Integer major, Integer minor, Integer patch) {
        this._major = major;
        this._minor = minor;
        this._patch = patch;
    }

    public static MinecraftVersion getVersion() {
        if (_instance == null) {
            _instance = parseVersion();
        }

        return _instance;
    }

    private static MinecraftVersion _instance;

    private static MinecraftVersion parseVersion() {
        var pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");
        var versions = new Integer[3];

        var matcher = pattern.matcher(Bukkit.getVersion());
        if (matcher.find()) {
            versions[0] = Integer.parseInt(matcher.group(1));
            versions[1] = Integer.parseInt(matcher.group(2));
            versions[2] = Integer.parseInt(matcher.group(3));
        }
        else {
            versions[0] = 0;
            versions[1] = 0;
            versions[2] = 0;
        }

        return new MinecraftVersion(versions[0], versions[1], versions[2]);
    }

    public String getString() {
        return String.format("%s.%s.%s", this._major, this._minor, this._patch);
    }
}

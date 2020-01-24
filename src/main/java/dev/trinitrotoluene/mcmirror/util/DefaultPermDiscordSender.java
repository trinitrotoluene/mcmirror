package dev.trinitrotoluene.mcmirror.util;

import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DefaultPermDiscordSender implements RemoteConsoleCommandSender {
    private final Set<Permission> _perms;
    private final MinecraftMessageMirror _minecraftMirror;

    public DefaultPermDiscordSender(MinecraftMessageMirror minecraftMirror, FileConfiguration config) {
        this._minecraftMirror = minecraftMirror;

        var perms = new HashSet<Permission>();

        var section = config.getStringList("remote-execution.permissions.default");
        if (section.size() > 0) {
            for (var permName : section) {
                var perm = new Permission(permName, PermissionDefault.TRUE);
                perms.add(perm);
            }
        }

        this._perms = perms;
    }

    @Override
    public @NotNull String getName() {
        return "Discord-User";
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        for (var perm : this._perms) {
            if (perm.getName().equals(s)) {
                return perm.getDefault().getValue(false);
            }
        }
        return false;
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        for (var perm : this._perms) {
            if (perm.equals(permission)) {
                return perm.getDefault().getValue(false);
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        for (var perm : this._perms) {
            if (perm.getName().equals(s)) {
                return perm.getDefault().getValue(false);
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        for (var perm : this._perms) {
            if (perm.equals(permission)) {
                return perm.getDefault().getValue(false);
            }
        }
        return false;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void sendMessage(@NotNull String s) {
        this._minecraftMirror.sendSystemMessage(s);
    }

    @Override
    public void sendMessage(@NotNull String[] strings) {
        sendMessage(String.join("", strings));
    }

    @Override
    public @NotNull Server getServer() {
        return Bukkit.getServer();
    }

    /* Notimpl */

    @Override
    public void recalculatePermissions() {
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return null;
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return null;
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return null;
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return null;
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public @NotNull Spigot spigot() {
        return null;
    }

    @Override
    public void setOp(boolean b) {
    }
}

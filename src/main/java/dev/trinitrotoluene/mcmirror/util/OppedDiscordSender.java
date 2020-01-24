package dev.trinitrotoluene.mcmirror.util;

import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class OppedDiscordSender implements RemoteConsoleCommandSender {
    private final MinecraftMessageMirror _minecraftMirror;

    public OppedDiscordSender(MinecraftMessageMirror minecraftMirror, FileConfiguration config) {
        this._minecraftMirror = minecraftMirror;
    }

    @Override
    public @NotNull String getName() {
        return "Discord-OP";
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return true;
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return true;
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return true;
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return true;
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

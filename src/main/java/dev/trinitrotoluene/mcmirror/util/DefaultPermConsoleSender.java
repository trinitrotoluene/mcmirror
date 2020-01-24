package dev.trinitrotoluene.mcmirror.util;

import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DefaultPermConsoleSender implements ConsoleCommandSender {
    private final Set<Permission> _perms;
    private final MinecraftMessageMirror _minecraftMirror;

    public DefaultPermConsoleSender(MinecraftMessageMirror minecraftMirror, FileConfiguration config) {
        this._minecraftMirror = minecraftMirror;

        var perms = new HashSet<Permission>();
        perms.addAll(Bukkit.getPluginManager().getDefaultPermissions(false));

        var section = config.getConfigurationSection("permissions.default");
        if (section != null) {
            var values = Permission.loadPermissions(section.getValues(true), "permission %s is invalid", PermissionDefault.TRUE);
            perms.addAll(values);
        }

        this._perms = perms;
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
    public void sendRawMessage(@NotNull String s) {
        this._minecraftMirror.sendSystemMessage(s);
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void sendMessage(@NotNull String s) {
        sendRawMessage(s);
    }

    @Override
    public void sendMessage(@NotNull String[] strings) {
        sendRawMessage(String.join("", strings));
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
    public @NotNull String getName() {
        return "dev.trinitrotoluene.mcmirror.DefaultPermConsoleSender";
    }

    @Override
    public @NotNull Spigot spigot() {
        return null;
    }

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput(@NotNull String s) {
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return false;
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
    }

    @Override
    public void setOp(boolean b) {
    }
}

package me.manny.listener;

import me.manny.SendHub;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    private SendHub plugin;

    public PlayerListener(SendHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerKickEvent(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo kickedFrom;
        if (player.getServer() != null) {
            kickedFrom = player.getServer().getInfo();
        } else if (this.plugin.getProxy().getReconnectHandler() != null) {
            kickedFrom = this.plugin.getProxy().getReconnectHandler().getServer(player);
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(player.getPendingConnection());
            if (kickedFrom == null) {
                kickedFrom = ProxyServer.getInstance().getServerInfo(player.getPendingConnection().getListener().getDefaultServer());
            }
        }
        ServerInfo kickTo = this.plugin.getProxy().getServerInfo(plugin.getConfig().getString("fallback-server"));
        if (kickedFrom != null && kickedFrom.equals(kickTo)) {
            return;
        }
        event.setCancelled(true);
        event.setCancelServer(kickTo);
        if (!plugin.getConfig().getString("fallback-message").equals("")) {
            String kickReason = BaseComponent.toLegacyText(event.getKickReasonComponent());
            String message = this.plugin.getConfig().getString("fallback-message").replace("<fallback_server>", kickTo.getName()).replace("<reason>", kickReason);
            player.sendMessage(new TextComponent(message));
        }
    }
}
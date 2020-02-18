package me.manny;

import lombok.Getter;
import me.manny.listener.PlayerListener;
import me.manny.util.Config;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class SendHub extends Plugin {

    @Getter
    private static SendHub instance;
    private Config config;

    @Override
    public void onEnable() {
        instance = this;
        this.config = new Config(this, "config");
        this.getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
    }
}
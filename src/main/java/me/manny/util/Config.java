package me.manny.util;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private Configuration config;

    public Config(Plugin plugin, String name) {
        File file = new File(plugin.getDataFolder() + File.separator, name + ".yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            file = loadResource(plugin, name + ".yml");
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            System.out.println(ChatColor.RED + "[" + plugin.getDescription().getName() + "" + plugin.getDescription().getVersion() + "] was not able to load '" + file.getName() + "'. Printing stacktrace below..." + ChatColor.RESET);
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        if (config.getString(path).length() > 0) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(path));
        }
        return "ERROR: STRING NOT FOUND";
    }

    private File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
}
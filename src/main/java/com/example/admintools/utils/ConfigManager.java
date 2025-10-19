package com.example.admintools.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private FileConfiguration messagesConfig;
    private File configFile;
    private File messagesFile;
    private final Map<String, String> messages = new HashMap<>();
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
        createMessagesFile();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        loadMessagesConfig();
        loadMessages();
    }
    
    private void createMessagesFile() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
    }
    
    private void loadMessagesConfig() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }
    
    private void loadMessages() {
        messages.clear();
        
        // Загружаем все сообщения из messages.yml
        loadSection("", messagesConfig);
    }
    
    private void loadSection(String path, FileConfiguration config) {
        if (config.getConfigurationSection(path) == null) return;
        
        for (String key : config.getConfigurationSection(path).getKeys(false)) {
            String fullPath = path.isEmpty() ? key : path + "." + key;
            
            if (config.isConfigurationSection(fullPath)) {
                loadSection(fullPath, config);
            } else if (config.isString(fullPath)) {
                messages.put(fullPath, config.getString(fullPath));
            }
        }
    }
    
    public String getMessage(String path) {
        return messages.getOrDefault(path, "&cСообщение не найдено: " + path)
                .replace("&", "§");
    }
    
    public String getMessage(String path, Map<String, String> placeholders) {
        String message = getMessage(path);
        
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        
        return message;
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
    
    public void saveMessagesConfig() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Не удалось сохранить messages.yml: " + e.getMessage());
        }
    }
}

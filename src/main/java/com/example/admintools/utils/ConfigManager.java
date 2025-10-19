package com.example.admintools.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final Map<String, String> messages = new HashMap<>();
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        loadMessages();
    }
    
    private void loadMessages() {
        messages.clear();
        
        // Загружаем все сообщения из конфига
        loadSection("", config);
    }
    
    private void loadSection(String path, FileConfiguration config) {
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
}

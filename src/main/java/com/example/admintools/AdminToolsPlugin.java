package com.example.admintools;

import com.example.admintools.chat.ChatListener;
import com.example.admintools.chat.TelegramManager;
import com.example.admintools.commands.CommandManager;
import com.example.admintools.particles.ParticleManager;
import com.example.admintools.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminToolsPlugin extends JavaPlugin {
    
    private static AdminToolsPlugin instance;
    private ConfigManager configManager;
    private ParticleManager particleManager;
    private CommandManager commandManager;
    private TelegramManager telegramManager;
    
@Override
public void onEnable() {
    instance = this;
    
    // Инициализация менеджеров
    this.configManager = new ConfigManager(this);
    this.particleManager = new ParticleManager(this);
    this.commandManager = new CommandManager(this);
    
    // Загрузка конфигурации
    configManager.saveDefaultConfig();
    configManager.reloadConfig(); // Теперь загружает и config.yml и messages.yml
    
    // Регистрация команд
    commandManager.registerCommands();
    
    // Инициализация телеграм бота если включен
    if (configManager.getConfig().getBoolean("telegram.enabled", false)) {
        this.telegramManager = new TelegramManager(this);
        telegramManager.startBot();
        
        // Регистрация слушателя чата
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }
    
    getLogger().info("AdminToolsPlugin включен!");
}
    
    @Override
    public void onDisable() {
        if (particleManager != null) {
            particleManager.disableAllParticles();
        }
        if (telegramManager != null) {
            telegramManager.stopBot();
        }
        getLogger().info("AdminToolsPlugin выключен!");
    }
    
    public static AdminToolsPlugin getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public ParticleManager getParticleManager() {
        return particleManager;
    }
    
    public TelegramManager getTelegramManager() {
        return telegramManager;
    }
}

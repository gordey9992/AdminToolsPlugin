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
        
        // Красивое приветствие в консоль
        printWelcomeMessage();
        
        // Инициализация менеджеров
        this.configManager = new ConfigManager(this);
        this.particleManager = new ParticleManager(this);
        this.commandManager = new CommandManager(this);
        
        // Загрузка конфигурации
        configManager.saveDefaultConfig();
        configManager.reloadConfig();
        
        // Регистрация команд
        commandManager.registerCommands();
        
        // Инициализация телеграм бота если включен
        if (configManager.getConfig().getBoolean("telegram.enabled", false)) {
            this.telegramManager = new TelegramManager(this);
            telegramManager.startBot();
            
            // Регистрация слушателя чата
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
        
        getLogger().info("AdminToolsPlugin успешно включен!");
    }
    
    @Override
    public void onDisable() {
        if (particleManager != null) {
            particleManager.disableAllParticles();
        }
        if (telegramManager != null) {
            telegramManager.stopBot();
        }
        
        // Прощальное сообщение
        printGoodbyeMessage();
        
        getLogger().info("AdminToolsPlugin выключен!");
    }
    
    private void printWelcomeMessage() {
        System.out.println("");
        System.out.println("§a╔══════════════════════════════════════════════════════════════╗".replace("§", ""));
        System.out.println("§a║                                                              ║".replace("§", ""));
        System.out.println("§a║    §6█████╗ ██████╗ ███╗   ███╗██╗███╗   ██╗               §a║".replace("§", ""));
        System.out.println("§a║   §6██╔══██╗██╔══██╗████╗ ████║██║████╗  ██║               §a║".replace("§", ""));
        System.out.println("§a║   §6███████║██║  ██║██╔████╔██║██║██╔██╗ ██║               §a║".replace("§", ""));
        System.out.println("§a║   §6██╔══██║██║  ██║██║╚██╔╝██║██║██║╚██╗██║               §a║".replace("§", ""));
        System.out.println("§a║   §6██║  ██║██████╔╝██║ ╚═╝ ██║██║██║ ╚████║               §a║".replace("§", ""));
        System.out.println("§a║   §6╚═╝  ╚═╝╚═════╝ ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝               §a║".replace("§", ""));
        System.out.println("§a║                                                              ║".replace("§", ""));
        System.out.println("§a║    §b████████╗ ██████╗  ██████╗ ██╗     ███████╗       §a║".replace("§", ""));
        System.out.println("§a║    §b╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝       §a║".replace("§", ""));
        System.out.println("§a║       §b██║   ██║   ██║██║   ██║██║     ███████╗       §a║".replace("§", ""));
        System.out.println("§a║       §b██║   ██║   ██║██║   ██║██║     ╚════██║       §a║".replace("§", ""));
        System.out.println("§a║       §b██║   ╚██████╔╝╚██████╔╝███████╗███████║       §a║".replace("§", ""));
        System.out.println("§a║       §b╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝       §a║".replace("§", ""));
        System.out.println("§a║                                                              ║".replace("§", ""));
        System.out.println("§a║     §e🚀 §fAdminToolsPlugin §e1.0.0 §f- Успешно загружен! §e🚀     §a║".replace("§", ""));
        System.out.println("§a║                                                              ║".replace("§", ""));
        System.out.println("§a║  §f⭐ §b40+ русских команд           §f⭐ §bСистема частиц        §a║".replace("§", ""));
        System.out.println("§a║  §f⭐ §bTelegram синхронизация       §f⭐ §bВолшебная рыба        §a║".replace("§", ""));
        System.out.println("§a║  §f⭐ §bАдминские инструменты       §f⭐ §bМодерация             §a║".replace("§", ""));
        System.out.println("§a║                                                              ║".replace("§", ""));
        System.out.println("§a║              §dСоздано с ❤️ для вашего сервера! §d             §a║".replace("§", ""));
        System.out.println("§a║                                                              ║".replace("§", ""));
        System.out.println("§a╚══════════════════════════════════════════════════════════════╝".replace("§", ""));
        System.out.println("");
    }
    
    private void printGoodbyeMessage() {
        System.out.println("");
        System.out.println("§c╔══════════════════════════════════════════════════════════════╗".replace("§", ""));
        System.out.println("§c║                                                              ║".replace("§", ""));
        System.out.println("§c║    §4█████╗ ██████╗ ███╗   ███╗██╗███╗   ██╗               §c║".replace("§", ""));
        System.out.println("§c║   §4██╔══██╗██╔══██╗████╗ ████║██║████╗  ██║               §c║".replace("§", ""));
        System.out.println("§c║   §4███████║██║  ██║██╔████╔██║██║██╔██╗ ██║               §c║".replace("§", ""));
        System.out.println("§c║   §4██╔══██║██║  ██║██║╚██╔╝██║██║██║╚██╗██║               §c║".replace("§", ""));
        System.out.println("§c║   §4██║  ██║██████╔╝██║ ╚═╝ ██║██║██║ ╚████║               §c║".replace("§", ""));
        System.out.println("§c║   §4╚═╝  ╚═╝╚═════╝ ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝               §c║".replace("§", ""));
        System.out.println("§c║                                                              ║".replace("§", ""));
        System.out.println("§c║    §6████████╗ ██████╗  ██████╗ ██╗     ███████╗       §c║".replace("§", ""));
        System.out.println("§c║    §6╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝       §c║".replace("§", ""));
        System.out.println("§c║       §6██║   ██║   ██║██║   ██║██║     ███████╗       §c║".replace("§", ""));
        System.out.println("§c║       §6██║   ██║   ██║██║   ██║██║     ╚════██║       §c║".replace("§", ""));
        System.out.println("§c║       §6██║   ╚██████╔╝╚██████╔╝███████╗███████║       §c║".replace("§", ""));
        System.out.println("§c║       §6╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝       §c║".replace("§", ""));
        System.out.println("§c║                                                              ║".replace("§", ""));
        System.out.println("§c║           §fAdminToolsPlugin §c- §fВыключен! §c😢               §c║".replace("§", ""));
        System.out.println("§c║                                                              ║".replace("§", ""));
        System.out.println("§c║              §fСпасибо за использование! §c❤️                 §c║".replace("§", ""));
        System.out.println("§c║         §fДо скорых встреч на сервере! §c🎮                  §c║".replace("§", ""));
        System.out.println("§c║                                                              ║".replace("§", ""));
        System.out.println("§c╚══════════════════════════════════════════════════════════════╝".replace("§", ""));
        System.out.println("");
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
    
    public CommandManager getCommandManager() {
        return commandManager;
    }
    
    public TelegramManager getTelegramManager() {
        return telegramManager;
    }
}

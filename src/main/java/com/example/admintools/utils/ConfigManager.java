package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.command.PluginCommand;

public class CommandManager {
    
    private final AdminToolsPlugin plugin;
    private DirectCommands directCommands;
    
    public CommandManager(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void registerCommands() {
        this.directCommands = new DirectCommands(plugin);
        
        // Регистрируем команду частиц
        PluginCommand setParticleCommand = plugin.getCommand("setparticle");
        if (setParticleCommand != null) {
            SetParticleCommand particleExecutor = new SetParticleCommand(plugin);
            setParticleCommand.setExecutor(particleExecutor);
            setParticleCommand.setTabCompleter(particleExecutor);
        }
        
        // Регистрируем ВСЕ прямые команды
        String[] directCommandNames = {
            "рыба", "полет", "исцелить", "еда", "бог", "невидимость", "скорость", 
            "убить", "кик", "мут", "очистить", "огонь", "молния", "время", 
            "погода", "инвентарь", "верстак", "эндсундук", "верх", "череп",
            "скоростьполета", "скоростьходьбы", "шляпа", "назад", "объявление",
            "сказать", "судо", "починить", "режим", "телепорт", "суперсила",
            "лазер", "телекинез", "торнадо", "антигравитация", "радуга", 
            "метеорит", "полныйремонт"
        };
        
        for (String cmdName : directCommandNames) {
            PluginCommand directCommand = plugin.getCommand(cmdName);
            if (directCommand != null) {
                directCommand.setExecutor(directCommands);
            }
        }
    }
}

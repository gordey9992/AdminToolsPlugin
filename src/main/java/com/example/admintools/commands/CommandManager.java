package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.command.PluginCommand;

public class CommandManager {
    
    private final AdminToolsPlugin plugin;
    private AdminCommand adminCommand;
    private DirectCommands directCommands;
    
    public CommandManager(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void registerCommands() {
        // Сначала создаем основной командный класс
        this.adminCommand = new AdminCommand(plugin);
        this.directCommands = new DirectCommands(plugin, adminCommand);
        
        // Регистрируем команду частиц
        PluginCommand setParticleCommand = plugin.getCommand("setparticle");
        if (setParticleCommand != null) {
            SetParticleCommand particleExecutor = new SetParticleCommand(plugin);
            setParticleCommand.setExecutor(particleExecutor);
            setParticleCommand.setTabCompleter(particleExecutor);
        }
        
        // Регистрируем админские команды
        String[] adminCommands = {"admin", "admintools", "at"};
        for (String cmdName : adminCommands) {
            PluginCommand adminCommand = plugin.getCommand(cmdName);
            if (adminCommand != null) {
                adminCommand.setExecutor(this.adminCommand);
                adminCommand.setTabCompleter(this.adminCommand);
            }
        }
        
        // Регистрируем волшебную рыбу
        PluginCommand fishCommand = plugin.getCommand("рыба");
        if (fishCommand != null) {
            fishCommand.setExecutor(directCommands);
        }
        
        // Регистрируем ВСЕ прямые команды на русском (50+ команд!)
        String[] directCommandNames = {
            "полет", "исцелить", "еда", "бог", "невидимость", "скорость", 
            "убить", "кик", "мут", "очистить", "огонь", "молния", "время", 
            "погода", "инвентарь", "верстак", "эндсундук", "верх", "череп",
            "скоростьполета", "скоростьходьбы", "шляпа", "назад", "объявление",
            "сказать", "судо", "починить", "режим", "телепорт", "полныйремонт",
            "летающаярыба", "ночноезрение", "взрыв", "полныйреген", "суперпрыжок",
            "клон", "радуга", "метеорит", "инвиз", "полетрыбы",
            // ЕЩЕ БОЛЬШЕ КОМАНД!
            "полетv2", "суперсила", "водопад", "лазер", "телекинез", "заморозка",
            "торнадо", "землетрясение", "нектар", "радиация", "антигравитация",
            "телепортация", "неуязвимость", "сверхскорость", "гигант", "карлик",
            "невидимка", "свет", "тьма", "буря", "радугаv2", "фейерверк", "портал"
        };
        
        for (String cmdName : directCommandNames) {
            PluginCommand directCommand = plugin.getCommand(cmdName);
            if (directCommand != null) {
                directCommand.setExecutor(directCommands);
            }
        }
    }
}

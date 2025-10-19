package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.command.PluginCommand;

public class CommandManager {
    
    private final AdminToolsPlugin plugin;
    
    public CommandManager(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void registerCommands() {
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
                AdminCommand adminExecutor = new AdminCommand(plugin);
                adminCommand.setExecutor(adminExecutor);
                adminCommand.setTabCompleter(adminExecutor);
            }
        }
    }
}

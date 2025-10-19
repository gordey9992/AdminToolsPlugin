package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetParticleCommand implements CommandExecutor, TabCompleter {
    
    private final AdminToolsPlugin plugin;
    
    public SetParticleCommand(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("admintools.particle")) {
            player.sendMessage("§cУ вас нет разрешения на использование этой команды!");
            return true;
        }
        
        if (args.length == 0) {
            // Включить/выключить частицы
            if (plugin.getParticleManager().hasParticleTrail(player)) {
                plugin.getParticleManager().disableParticleTrail(player);
                player.sendMessage("§cСлед из частиц выключен");
            } else {
                player.sendMessage("§cИспользование: /setparticle [тип] [цвет] [размер]");
                player.sendMessage("§aДоступные частицы: " + plugin.getParticleManager().getAvailableParticles());
            }
            return true;
        }
        
        String particleType = args[0];
        String color = args.length > 1 ? args[1] : null;
        double size = args.length > 2 ? parseDouble(args[2]) : 1.0;
        
        plugin.getParticleManager().setParticleTrail(player, particleType, color, size);
        
        player.sendMessage("§aСлед из частиц включен! Тип: §6" + particleType);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Предлагаем популярные частицы
            String[] popularParticles = {
                "flame", "heart", "cloud", "crit", "smoke", "spell",
                "note", "portal", "redstone", "dust_color_transition",
                "electric_spark", "sculk_soul", "glow", "wax_on"
            };
            
            for (String particle : popularParticles) {
                if (particle.startsWith(args[0].toLowerCase())) {
                    completions.add(particle);
                }
            }
        } else if (args.length == 2) {
            // Предлагаем цвета
            String[] colors = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF"};
            for (String color : colors) {
                if (color.startsWith(args[1])) {
                    completions.add(color);
                }
            }
        } else if (args.length == 3) {
            // Предлагаем размеры
            String[] sizes = {"0.5", "1.0", "1.5", "2.0", "2.5", "3.0"};
            for (String size : sizes) {
                if (size.startsWith(args[2])) {
                    completions.add(size);
                }
            }
        }
        
        return completions;
    }
    
    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 1.0;
        }
    }
}

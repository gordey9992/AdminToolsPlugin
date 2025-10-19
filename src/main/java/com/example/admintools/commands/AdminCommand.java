package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import com.example.admintools.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class AdminCommand implements CommandExecutor, TabCompleter {
    
    private final AdminToolsPlugin plugin;
    private final ConfigManager configManager;
    private final Map<UUID, Location> lastLocations = new HashMap<>();
    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private final Set<UUID> mutedPlayers = new HashSet<>();
    private final Set<UUID> godPlayers = new HashSet<>();
    
    public AdminCommand(AdminToolsPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "fly":
            case "полет":
                handleFly(sender, args);
                break;
            case "gamemode":
            case "gm":
            case "режим":
                handleGamemode(sender, args);
                break;
            case "teleport":
            case "tp":
            case "телепорт":
                handleTeleport(sender, args);
                break;
            case "heal":
            case "исцелить":
                handleHeal(sender, args);
                break;
            case "feed":
            case "еда":
                handleFeed(sender, args);
                break;
            case "god":
            case "бог":
                handleGod(sender, args);
                break;
            case "vanish":
            case "v":
            case "невидимость":
                handleVanish(sender, args);
                break;
            case "speed":
            case "скорость":
                handleSpeed(sender, args);
                break;
            case "kill":
            case "убить":
                handleKill(sender, args);
                break;
            case "kick":
            case "кик":
                handleKick(sender, args);
                break;
            case "mute":
            case "мьют":
                handleMute(sender, args);
                break;
            case "clear":
            case "очистить":
                handleClear(sender, args);
                break;
            case "burn":
            case "огонь":
                handleBurn(sender, args);
                break;
            case "strike":
            case "молния":
                handleStrike(sender, args);
                break;
            case "time":
            case "время":
                handleTime(sender, args);
                break;
            case "weather":
            case "погода":
                handleWeather(sender, args);
                break;
            case "invsee":
            case "инвентарь":
                handleInvsee(sender, args);
                break;
            case "workbench":
            case "верстак":
                handleWorkbench(sender, args);
                break;
            case "enderchest":
            case "эндсундук":
                handleEnderchest(sender, args);
                break;
            case "top":
            case "верх":
                handleTop(sender, args);
                break;
            case "skull":
            case "череп":
                handleSkull(sender, args);
                break;
            case "flyspeed":
            case "скоростьполета":
                handleFlySpeed(sender, args);
                break;
            case "walkspeed":
            case "скоростьходьбы":
                handleWalkSpeed(sender, args);
                break;
            case "hat":
            case "шляпа":
                handleHat(sender, args);
                break;
            case "back":
            case "назад":
                handleBack(sender, args);
                break;
            case "broadcast":
            case "bc":
            case "объявление":
                handleBroadcast(sender, args);
                break;
            case "say":
            case "сказать":
                handleSay(sender, args);
                break;
            case "sudo":
            case "судо":
                handleSudo(sender, args);
                break;
            case "repair":
            case "починить":
                handleRepair(sender, args);
                break;
            default:
                sender.sendMessage(configManager.getMessage("invalid-arguments"));
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(configManager.getMessage("help-header"));
        
        // Список всех команд с описанием
        String[][] commands = {
            {"fly [игрок]", "Включить/выключить полет"},
            {"gamemode <режим> [игрок]", "Изменить режим игры"},
            {"teleport <игрок/xyz>", "Телепортироваться"},
            {"heal [игрок]", "Исцелить игрока"},
            {"feed [игрок]", "Накормить игрока"},
            {"god [игрок]", "Режим бога"},
            {"vanish", "Невидимость"},
            {"speed <1-10> [игрок]", "Изменить скорость"},
            {"kill [игрок]", "Убить игрока"},
            {"kick <игрок> [причина]", "Кикнуть игрока"},
            {"mute <игрок>", "Заглушить игрока"},
            {"clear [игрок]", "Очистить инвентарь"},
            {"burn <игрок> [секунды]", "Поджечь игрока"},
            {"strike <игрок>", "Ударить молнией"},
            {"time <время>", "Изменить время"},
            {"weather <погода>", "Изменить погоду"},
            {"invsee <игрок>", "Посмотреть инвентарь"},
            {"workbench", "Открыть верстак"},
            {"enderchest [игрок]", "Открыть эндерсундук"},
            {"top", "Телепорт на верх"},
            {"skull <игрок>", "Получить череп"},
            {"flyspeed <1-10> [игрок]", "Скорость полета"},
            {"walkspeed <1-10> [игрок]", "Скорость ходьбы"},
            {"hat", "Надеть шляпу"},
            {"back", "Вернуться назад"},
            {"broadcast <сообщение>", "Объявление"},
            {"say <сообщение>", "Сказать от имени сервера"},
            {"sudo <игрок> <команда/сообщение>", "Заставить выполнить"},
            {"repair [all]", "Починить предмет"}
        };
        
        for (String[] cmd : commands) {
            sender.sendMessage(configManager.getMessage("help-format", 
                Map.of("command", "admin " + cmd[0], "description", cmd[1])));
        }
    }
    
    // Реализации всех методов handle...
    // Из-за ограничения длины покажу несколько примеров:
    
    private void handleFly(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 2) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        boolean flying = !target.getAllowFlight();
        target.setAllowFlight(flying);
        target.setFlying(flying);
        
        if (target == sender) {
            target.sendMessage(flying ? 
                configManager.getMessage("fly.enabled-self") : 
                configManager.getMessage("fly.disabled-self"));
        } else {
            sender.sendMessage(configManager.getMessage(flying ? "fly.enabled" : "fly.disabled", 
                Map.of("player", target.getName())));
            target.sendMessage(configManager.getMessage(flying ? "fly.enabled-self" : "fly.disabled-self"));
        }
    }
    
    private void handleGamemode(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin gamemode <survival/creative/adventure/spectator> [игрок]")));
            return;
        }
        
        GameMode gamemode = parseGamemode(args[1]);
        if (gamemode == null) {
            sender.sendMessage(configManager.getMessage("gamemode.invalid"));
            return;
        }
        
        Player target = getTarget(sender, args, 2);
        if (target == null) return;
        
        target.setGameMode(gamemode);
        
        if (target == sender) {
            target.sendMessage(configManager.getMessage("gamemode.changed-self", 
                Map.of("gamemode", gamemode.name().toLowerCase())));
        } else {
            sender.sendMessage(configManager.getMessage("gamemode.changed", 
                Map.of("gamemode", gamemode.name().toLowerCase(), "player", target.getName())));
            target.sendMessage(configManager.getMessage("gamemode.changed-self", 
                Map.of("gamemode", gamemode.name().toLowerCase())));
        }
    }
    
    private void handleTeleport(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin teleport <игрок/x y z>")));
            return;
        }
        
        // Сохраняем предыдущую локацию
        lastLocations.put(player.getUniqueId(), player.getLocation());
        
        // Попытка телепорта к игроку
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer != null) {
            player.teleport(targetPlayer.getLocation());
            player.sendMessage(configManager.getMessage("teleport.teleported", 
                Map.of("player", targetPlayer.getName())));
            return;
        }
        
        // Попытка телепорта по координатам
        if (args.length >= 4) {
            try {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                
                Location location = new Location(player.getWorld(), x, y, z);
                player.teleport(location);
                
                player.sendMessage(configManager.getMessage("teleport.location", 
                    Map.of("x", String.valueOf(x), "y", String.valueOf(y), "z", String.valueOf(z))));
                return;
            } catch (NumberFormatException e) {
                // Не координаты
            }
        }
        
        sender.sendMessage(configManager.getMessage("player-not-found", 
            Map.of("player", args[1])));
    }
    
    // ... остальные методы handle (heal, feed, god, и т.д.)
    
    private Player getTarget(CommandSender sender, String[] args, int argIndex) {
        if (args.length > argIndex) {
            Player target = Bukkit.getPlayer(args[argIndex]);
            if (target == null) {
                sender.sendMessage(configManager.getMessage("player-not-found", 
                    Map.of("player", args[argIndex])));
                return null;
            }
            return target;
        }
        
        if (sender instanceof Player) {
            return (Player) sender;
        }
        
        sender.sendMessage(configManager.getMessage("player-only"));
        return null;
    }
    
    private GameMode parseGamemode(String input) {
        return switch (input.toLowerCase()) {
            case "0", "survival", "s", "выживание" -> GameMode.SURVIVAL;
            case "1", "creative", "c", "творчество" -> GameMode.CREATIVE;
            case "2", "adventure", "a", "приключение" -> GameMode.ADVENTURE;
            case "3", "spectator", "sp", "наблюдатель" -> GameMode.SPECTATOR;
            default -> null;
        };
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Основные команды
            String[] subCommands = {
                "fly", "gamemode", "teleport", "heal", "feed", "god", "vanish", 
                "speed", "kill", "kick", "mute", "clear", "burn", "strike",
                "time", "weather", "invsee", "workbench", "enderchest", "top",
                "skull", "flyspeed", "walkspeed", "hat", "back", "broadcast",
                "say", "sudo", "repair"
            };
            
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        } else if (args.length == 2) {
            // Автодополнение для конкретных команд
            switch (args[0].toLowerCase()) {
                case "gamemode":
                    String[] gamemodes = {"survival", "creative", "adventure", "spectator"};
                    Collections.addAll(completions, gamemodes);
                    break;
                case "teleport":
                case "tp":
                case "invsee":
                case "kick":
                case "ban":
                case "mute":
                case "heal":
                case "feed":
                case "god":
                case "kill":
                case "burn":
                case "strike":
                case "skull":
                    // Имена игроков
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completions.add(player.getName());
                        }
                    }
                    break;
                case "time":
                    String[] times = {"day", "night", "noon", "midnight"};
                    Collections.addAll(completions, times);
                    break;
                case "weather":
                    String[] weathers = {"clear", "rain", "thunder"};
                    Collections.addAll(completions, weathers);
                    break;
            }
        }
        
        return completions;
    }
}

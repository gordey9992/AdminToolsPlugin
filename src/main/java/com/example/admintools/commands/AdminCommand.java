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
    
    // Поддержка русского и английского
    switch (subCommand) {
        // Fly / Полет
        case "fly": case "полет":
            handleFly(sender, args);
            break;
            
        // Gamemode / Режим
        case "gamemode": case "gm": case "режим":
            handleGamemode(sender, args);
            break;
            
        // Teleport / Телепорт
        case "teleport": case "tp": case "телепорт":
            handleTeleport(sender, args);
            break;
            
        // Heal / Исцелить
        case "heal": case "исцелить": case "хил":
            handleHeal(sender, args);
            break;
            
        // Feed / Еда
        case "feed": case "еда": case "покормить":
            handleFeed(sender, args);
            break;
            
        // God / Бог
        case "god": case "бог": case "бессмертие":
            handleGod(sender, args);
            break;
            
        // Vanish / Невидимость
        case "vanish": case "v": case "невидимость": case "невидимка":
            handleVanish(sender, args);
            break;
            
        // Speed / Скорость
        case "speed": case "скорость":
            handleSpeed(sender, args);
            break;
            
        // Kill / Убить
        case "kill": case "убить": case "слай":
            handleKill(sender, args);
            break;
            
        // Kick / Кик
        case "kick": case "кик": case "кикнуть":
            handleKick(sender, args);
            break;
            
        // Mute / Мут
        case "mute": case "мьют": case "мут": case "заглушить":
            handleMute(sender, args);
            break;
            
        // Clear / Очистить
        case "clear": case "очистить": case "клир":
            handleClear(sender, args);
            break;
            
        // Burn / Огонь
        case "burn": case "огонь": case "поджечь":
            handleBurn(sender, args);
            break;
            
        // Strike / Молния
        case "strike": case "молния": case "удар":
            handleStrike(sender, args);
            break;
            
        // Time / Время
        case "time": case "время":
            handleTime(sender, args);
            break;
            
        // Weather / Погода
        case "weather": case "погода":
            handleWeather(sender, args);
            break;
            
        // Invsee / Инвентарь
        case "invsee": case "инвентарь": case "инв":
            handleInvsee(sender, args);
            break;
            
        // Workbench / Верстак
        case "workbench": case "верстак": case "верстак":
            handleWorkbench(sender, args);
            break;
            
        // Enderchest / Эндерсундук
        case "enderchest": case "эндсундук": case "эш":
            handleEnderchest(sender, args);
            break;
            
        // Top / Верх
        case "top": case "верх":
            handleTop(sender, args);
            break;
            
        // Skull / Череп
        case "skull": case "череп": case "голова":
            handleSkull(sender, args);
            break;
            
        // Flyspeed / Скоростьполета
        case "flyspeed": case "скоростьполета": case "скоростьполёта":
            handleFlySpeed(sender, args);
            break;
            
        // Walkspeed / Скоростьходьбы
        case "walkspeed": case "скоростьходьбы": case "скоростьходу":
            handleWalkSpeed(sender, args);
            break;
            
        // Hat / Шляпа
        case "hat": case "шляпа": case "шапка":
            handleHat(sender, args);
            break;
            
        // Back / Назад
        case "back": case "назад": case "вернуться":
            handleBack(sender, args);
            break;
            
        // Broadcast / Объявление
        case "broadcast": case "bc": case "объявление": case "объявить":
            handleBroadcast(sender, args);
            break;
            
        // Say / Сказать
        case "say": case "сказать": case "сервер":
            handleSay(sender, args);
            break;
            
        // Sudo / Судо
        case "sudo": case "судо": case "заставить":
            handleSudo(sender, args);
            break;
            
        // Repair / Починить
        case "repair": case "починить": case "ремонт":
            handleRepair(sender, args);
            break;
            
        // Help / Помощь
        case "help": case "помощь": case "хелп":
            sendHelp(sender);
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
    
    // === РЕАЛИЗАЦИЯ ВСЕХ МЕТОДОВ HANDLE ===
    
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
    
    private void handleHeal(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20);
        target.setSaturation(10);
        target.setFireTicks(0);
        
        if (target == sender) {
            target.sendMessage(configManager.getMessage("heal.healed-self"));
        } else {
            sender.sendMessage(configManager.getMessage("heal.healed", 
                Map.of("player", target.getName())));
            target.sendMessage(configManager.getMessage("heal.healed-self"));
        }
    }
    
    private void handleFeed(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        target.setFoodLevel(20);
        target.setSaturation(10);
        
        if (target == sender) {
            target.sendMessage(configManager.getMessage("feed.fed-self"));
        } else {
            sender.sendMessage(configManager.getMessage("feed.fed", 
                Map.of("player", target.getName())));
            target.sendMessage(configManager.getMessage("feed.fed-self"));
        }
    }
    
    private void handleGod(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        boolean godMode = !godPlayers.contains(target.getUniqueId());
        
        if (godMode) {
            godPlayers.add(target.getUniqueId());
            target.setInvulnerable(true);
        } else {
            godPlayers.remove(target.getUniqueId());
            target.setInvulnerable(false);
        }
        
        if (target == sender) {
            target.sendMessage(godMode ? 
                configManager.getMessage("god.enabled-self") : 
                configManager.getMessage("god.disabled-self"));
        } else {
            sender.sendMessage(configManager.getMessage(godMode ? "god.enabled" : "god.disabled", 
                Map.of("player", target.getName())));
            target.sendMessage(configManager.getMessage(godMode ? "god.enabled-self" : "god.disabled-self"));
        }
    }
    
    private void handleVanish(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        boolean vanished = !vanishedPlayers.contains(player.getUniqueId());
        
        if (vanished) {
            vanishedPlayers.add(player.getUniqueId());
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("admintools.vanish")) {
                    online.hidePlayer(plugin, player);
                }
            }
            player.sendMessage(configManager.getMessage("vanish.enabled"));
        } else {
            vanishedPlayers.remove(player.getUniqueId());
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(plugin, player);
            }
            player.sendMessage(configManager.getMessage("vanish.disabled"));
        }
    }
    
    private void handleSpeed(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin speed <1-10> [игрок]")));
            return;
        }
        
        try {
            float speed = Float.parseFloat(args[1]) / 10f;
            if (speed < 0.1f || speed > 1.0f) {
                sender.sendMessage(configManager.getMessage("speed.invalid"));
                return;
            }
            
            Player target = getTarget(sender, args, 2);
            if (target == null) return;
            
            target.setWalkSpeed(speed);
            target.setFlySpeed(speed);
            
            sender.sendMessage(configManager.getMessage("speed.set", 
                Map.of("speed", args[1], "player", target.getName())));
                
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.getMessage("speed.invalid"));
        }
    }
    
    private void handleKill(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        target.setHealth(0);
        
        if (target == sender) {
            target.sendMessage(configManager.getMessage("kill.killed-self"));
        } else {
            sender.sendMessage(configManager.getMessage("kill.killed", 
                Map.of("player", target.getName())));
        }
    }
    
    private void handleKick(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin kick <игрок> [причина]")));
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(configManager.getMessage("player-not-found", 
                Map.of("player", args[1])));
            return;
        }
        
        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "Не указана";
        
        target.kickPlayer(configManager.getMessage("kick.kicked-message", 
            Map.of("reason", reason)));
            
        sender.sendMessage(configManager.getMessage("kick.kicked", 
            Map.of("player", target.getName(), "reason", reason)));
    }
    
    private void handleMute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin mute <игрок>")));
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(configManager.getMessage("player-not-found", 
                Map.of("player", args[1])));
            return;
        }
        
        boolean muted = !mutedPlayers.contains(target.getUniqueId());
        
        if (muted) {
            mutedPlayers.add(target.getUniqueId());
            sender.sendMessage(configManager.getMessage("mute.muted", 
                Map.of("player", target.getName())));
        } else {
            mutedPlayers.remove(target.getUniqueId());
            sender.sendMessage(configManager.getMessage("mute.unmuted", 
                Map.of("player", target.getName())));
        }
    }
    
    private void handleClear(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        target.getInventory().clear();
        
        if (target == sender) {
            target.sendMessage(configManager.getMessage("clear.cleared-self"));
        } else {
            sender.sendMessage(configManager.getMessage("clear.cleared", 
                Map.of("player", target.getName())));
            target.sendMessage(configManager.getMessage("clear.cleared-self"));
        }
    }
    
    private void handleBurn(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin burn <игрок> [секунды]")));
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(configManager.getMessage("player-not-found", 
                Map.of("player", args[1])));
            return;
        }
        
        int seconds = args.length > 2 ? Integer.parseInt(args[2]) : 10;
        target.setFireTicks(seconds * 20);
        
        sender.sendMessage(configManager.getMessage("burn.burning", 
            Map.of("player", target.getName(), "seconds", String.valueOf(seconds))));
    }
    
    private void handleStrike(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin strike <игрок>")));
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(configManager.getMessage("player-not-found", 
                Map.of("player", args[1])));
            return;
        }
        
        target.getWorld().strikeLightning(target.getLocation());
        sender.sendMessage(configManager.getMessage("strike.struck", 
            Map.of("player", target.getName())));
    }
    
    private void handleTime(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin time <day/night/noon/midnight/число>")));
            return;
        }
        
        World world = sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().get(0);
        long time;
        
        switch (args[1].toLowerCase()) {
            case "day": time = 1000; break;
            case "night": time = 13000; break;
            case "noon": time = 6000; break;
            case "midnight": time = 18000; break;
            default:
                try {
                    time = Long.parseLong(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(configManager.getMessage("time.invalid"));
                    return;
                }
        }
        
        world.setTime(time);
        sender.sendMessage(configManager.getMessage("time.set", 
            Map.of("time", args[1])));
    }
    
    private void handleWeather(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin weather <clear/rain/thunder>")));
            return;
        }
        
        World world = sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().get(0);
        
        switch (args[1].toLowerCase()) {
            case "clear":
                world.setStorm(false);
                world.setThundering(false);
                break;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                break;
            case "thunder":
                world.setStorm(true);
                world.setThundering(true);
                break;
            default:
                sender.sendMessage(configManager.getMessage("weather.invalid"));
                return;
        }
        
        sender.sendMessage(configManager.getMessage("weather.set", 
            Map.of("weather", args[1])));
    }
    
    private void handleInvsee(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin invsee <игрок>")));
            return;
        }
        
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);
        
        if (target == null) {
            sender.sendMessage(configManager.getMessage("player-not-found", 
                Map.of("player", args[1])));
            return;
        }
        
        player.openInventory(target.getInventory());
        sender.sendMessage(configManager.getMessage("invsee.opened", 
            Map.of("player", target.getName())));
    }
    
    private void handleWorkbench(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        player.openWorkbench(null, true);
        player.sendMessage(configManager.getMessage("workbench.opened"));
    }
    
    private void handleEnderchest(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        player.openInventory(target.getEnderChest());
        player.sendMessage(configManager.getMessage("enderchest.opened", 
            Map.of("player", target.getName())));
    }
    
    private void handleTop(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Location location = player.getLocation();
        Location highest = location.getWorld().getHighestBlockAt(location).getLocation();
        highest.setY(highest.getY() + 1);
        
        player.teleport(highest);
        player.sendMessage(configManager.getMessage("top.teleported"));
    }
    
    private void handleSkull(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin skull <игрок>")));
            return;
        }
        
        Player player = (Player) sender;
        ItemStack skull = new ItemStack(org.bukkit.Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(args[1]));
        skull.setItemMeta(meta);
        
        player.getInventory().addItem(skull);
        player.sendMessage(configManager.getMessage("skull.given", 
            Map.of("player", args[1])));
    }
    
    private void handleFlySpeed(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin flyspeed <1-10> [игрок]")));
            return;
        }
        
        try {
            float speed = Float.parseFloat(args[1]) / 10f;
            if (speed < 0.1f || speed > 1.0f) {
                sender.sendMessage(configManager.getMessage("speed.invalid"));
                return;
            }
            
            Player target = getTarget(sender, args, 2);
            if (target == null) return;
            
            target.setFlySpeed(speed);
            sender.sendMessage(configManager.getMessage("flyspeed.set", 
                Map.of("speed", args[1], "player", target.getName())));
                
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.getMessage("speed.invalid"));
        }
    }
    
    private void handleWalkSpeed(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin walkspeed <1-10> [игрок]")));
            return;
        }
        
        try {
            float speed = Float.parseFloat(args[1]) / 10f;
            if (speed < 0.1f || speed > 1.0f) {
                sender.sendMessage(configManager.getMessage("speed.invalid"));
                return;
            }
            
            Player target = getTarget(sender, args, 2);
            if (target == null) return;
            
            target.setWalkSpeed(speed);
            sender.sendMessage(configManager.getMessage("walkspeed.set", 
                Map.of("speed", args[1], "player", target.getName())));
                
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.getMessage("speed.invalid"));
        }
    }
    
    private void handleHat(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        ItemStack hand = player.getInventory().getItemInMainHand();
        
        if (hand.getType().isAir()) {
            sender.sendMessage("§cВозьмите предмет в руку!");
            return;
        }
        
        player.getInventory().setHelmet(hand);
        player.getInventory().setItemInMainHand(null);
        player.sendMessage(configManager.getMessage("hat.equipped"));
    }
    
    private void handleBack(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Location lastLocation = lastLocations.get(player.getUniqueId());
        
        if (lastLocation == null) {
            player.sendMessage(configManager.getMessage("back.no-location"));
            return;
        }
        
        player.teleport(lastLocation);
        player.sendMessage(configManager.getMessage("back.teleported"));
    }
    
    private void handleBroadcast(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin broadcast <сообщение>")));
            return;
        }
        
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Bukkit.broadcastMessage(configManager.getMessage("broadcast.broadcasted", 
            Map.of("message", message)));
    }
    
    private void handleSay(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin say <сообщение>")));
            return;
        }
        
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Bukkit.broadcastMessage(configManager.getMessage("say.said", 
            Map.of("message", message)));
    }
    
    private void handleSudo(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(configManager.getMessage("usage", 
                Map.of("usage", "/admin sudo <игрок> <команда/сообщение>")));
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(configManager.getMessage("player-not-found", 
                Map.of("player", args[1])));
            return;
        }
        
        String command = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        
        if (command.startsWith("/")) {
            // Выполнить команду
            target.performCommand(command.substring(1));
            sender.sendMessage(configManager.getMessage("sudo.forced", 
                Map.of("player", target.getName(), "command", command)));
        } else {
            // Отправить сообщение
            target.chat(command);
            sender.sendMessage(configManager.getMessage("sudo.forced-chat", 
                Map.of("player", target.getName(), "message", command)));
        }
    }
    
    private void handleRepair(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        boolean repairAll = args.length > 1 && "all".equalsIgnoreCase(args[1]);
        
        if (repairAll) {
            // Починить все предметы в инвентаре
            for (ItemStack item : target.getInventory().getContents()) {
                if (item != null && item.getType().getMaxDurability() > 0) {
                    item.setDurability((short) 0);
                }
            }
            for (ItemStack item : target.getInventory().getArmorContents()) {
                if (item != null && item.getType().getMaxDurability() > 0) {
                    item.setDurability((short) 0);
                }
            }
            
            if (target == sender) {
                target.sendMessage(configManager.getMessage("repair.repaired-all-self"));
            } else {
                sender.sendMessage(configManager.getMessage("repair.repaired-all", 
                    Map.of("player", target.getName())));
                target.sendMessage(configManager.getMessage("repair.repaired-all-self"));
            }
        } else {
            // Починить предмет в руке
            ItemStack hand = target.getInventory().getItemInMainHand();
            if (hand != null && hand.getType().getMaxDurability() > 0) {
                hand.setDurability((short) 0);
                
                if (target == sender) {
                    target.sendMessage(configManager.getMessage("repair.repaired-self"));
                } else {
                    sender.sendMessage(configManager.getMessage("repair.repaired", 
                        Map.of("player", target.getName())));
                    target.sendMessage(configManager.getMessage("repair.repaired-self"));
                }
            } else {
                sender.sendMessage("§cПредмет в руке нельзя починить!");
            }
        }
    }
    
    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===
    
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
            switch (args[0].toLowerCase()) {
                case "gamemode":
                    String[] gamemodes = {"survival", "creative", "adventure", "spectator"};
                    Collections.addAll(completions, gamemodes);
                    break;
                case "teleport":
                case "tp":
                case "invsee":
                case "kick":
                case "mute":
                case "heal":
                case "feed":
                case "god":
                case "kill":
                case "burn":
                case "strike":
                case "skull":
                case "sudo":
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
                case "repair":
                    completions.add("all");
                    break;
            }
        }
        
        return completions;
    }
}

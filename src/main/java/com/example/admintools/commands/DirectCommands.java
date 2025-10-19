package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class DirectCommands implements CommandExecutor {
    
    private final AdminToolsPlugin plugin;
    private final Map<UUID, Location> lastLocations = new HashMap<>();
    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private final Set<UUID> mutedPlayers = new HashSet<>();
    private final Set<UUID> godPlayers = new HashSet<>();
    
    public DirectCommands(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        
        switch (commandName) {
            case "рыба":
                giveMagicFish(sender, args);
                break;
            case "полет":
                handleFly(sender, args);
                break;
            case "исцелить":
                handleHeal(sender, args);
                break;
            case "еда":
                handleFeed(sender, args);
                break;
            case "бог":
                handleGod(sender, args);
                break;
            case "невидимость":
                handleVanish(sender, args);
                break;
            case "скорость":
                handleSpeed(sender, args);
                break;
            case "убить":
                handleKill(sender, args);
                break;
            case "кик":
                handleKick(sender, args);
                break;
            case "мут":
                handleMute(sender, args);
                break;
            case "очистить":
                handleClear(sender, args);
                break;
            case "огонь":
                handleBurn(sender, args);
                break;
            case "молния":
                handleStrike(sender, args);
                break;
            case "время":
                handleTime(sender, args);
                break;
            case "погода":
                handleWeather(sender, args);
                break;
            case "инвентарь":
                handleInvsee(sender, args);
                break;
            case "верстак":
                handleWorkbench(sender, args);
                break;
            case "эндсундук":
                handleEnderchest(sender, args);
                break;
            case "верх":
                handleTop(sender, args);
                break;
            case "череп":
                handleSkull(sender, args);
                break;
            case "скоростьполета":
                handleFlySpeed(sender, args);
                break;
            case "скоростьходьбы":
                handleWalkSpeed(sender, args);
                break;
            case "шляпа":
                handleHat(sender, args);
                break;
            case "назад":
                handleBack(sender, args);
                break;
            case "объявление":
                handleBroadcast(sender, args);
                break;
            case "сказать":
                handleSay(sender, args);
                break;
            case "судо":
                handleSudo(sender, args);
                break;
            case "починить":
                handleRepair(sender, args);
                break;
            case "режим":
                handleGamemode(sender, args);
                break;
            case "телепорт":
                handleTeleport(sender, args);
                break;
                
            // НОВЫЕ КРУТЫЕ КОМАНДЫ
            case "суперсила":
                handleSuperPower(sender, args);
                break;
            case "лазер":
                handleLaser(sender, args);
                break;
            case "телекинез":
                handleTelekinesis(sender, args);
                break;
            case "торнадо":
                handleTornado(sender, args);
                break;
            case "антигравитация":
                handleAntigravity(sender, args);
                break;
            case "радуга":
                handleRainbow(sender, args);
                break;
            case "метеорит":
                handleMeteor(sender, args);
                break;
            case "полныйремонт":
                handleFullRepair(sender, args);
                break;
                
            default:
                sender.sendMessage("§cНеизвестная команда: /" + commandName);
                break;
        }
        
        return true;
    }
    
    // === ВОЛШЕБНАЯ РЫБА ===
    private void giveMagicFish(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        ItemStack magicFish = createMagicFish();
        target.getInventory().addItem(magicFish);
        
        target.sendMessage("§6✨ Вы получили §bВолшебную Рыбу Исполнения Желаний§6!");
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        
        if (target != sender) {
            sender.sendMessage("§aВыдали Волшебную Рыбу игроку §6" + target.getName());
        }
    }
    
    private ItemStack createMagicFish() {
        ItemStack fish = new ItemStack(Material.TROPICAL_FISH);
        ItemMeta meta = fish.getItemMeta();
        
        meta.setDisplayName("§bВолшебная Рыба Исполнения Желаний");
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Магическая рыба, исполняющая желания!");
        lore.add("§6✨ Волшебная сила внутри!");
        meta.setLore(lore);
        
        fish.setItemMeta(meta);
        return fish;
    }
    
    // === ОСНОВНЫЕ КОМАНДЫ ===
    private void handleFly(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        boolean flying = !target.getAllowFlight();
        target.setAllowFlight(flying);
        if (flying) target.setFlying(true);
        
        if (target == sender) {
            target.sendMessage(flying ? "§aРежим полета включен!" : "§cРежим полета выключен!");
        } else {
            sender.sendMessage(flying ? "§aПолет включен для §6" + target.getName() : "§cПолет выключен для §6" + target.getName());
            target.sendMessage(flying ? "§aРежим полета включен!" : "§cРежим полета выключен!");
        }
    }
    
    private void handleHeal(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20);
        target.setSaturation(10);
        target.setFireTicks(0);
        
        if (target == sender) {
            target.sendMessage("§aВы исцелены!");
        } else {
            sender.sendMessage("§aИгрок §6" + target.getName() + " §aисцелен!");
            target.sendMessage("§aВы исцелены!");
        }
    }
    
    private void handleFeed(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.setFoodLevel(20);
        target.setSaturation(10);
        
        if (target == sender) {
            target.sendMessage("§aВы накормлены!");
        } else {
            sender.sendMessage("§aИгрок §6" + target.getName() + " §aнакормлен!");
            target.sendMessage("§aВы накормлены!");
        }
    }
    
    private void handleGod(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
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
            target.sendMessage(godMode ? "§aРежим бога включен!" : "§cРежим бога выключен!");
        } else {
            sender.sendMessage(godMode ? "§aРежим бога включен для §6" + target.getName() : "§cРежим бога выключен для §6" + target.getName());
            target.sendMessage(godMode ? "§aРежим бога включен!" : "§cРежим бога выключен!");
        }
    }
    
    private void handleVanish(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
sender.sendMessage("§cЭту команду могут использовать только игроки!");
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
            player.sendMessage("§aНевидимость включена!");
        } else {
            vanishedPlayers.remove(player.getUniqueId());
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(plugin, player);
            }
            player.sendMessage("§cНевидимость выключена!");
        }
    }
    
    private void handleSpeed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /скорость <1-10> [игрок]");
            return;
        }
        
        try {
            float speed = Float.parseFloat(args[0]) / 10f;
            if (speed < 0.1f || speed > 1.0f) {
                sender.sendMessage("§cНеверная скорость. Используйте число от 1 до 10");
                return;
            }
            
            Player target = getTarget(sender, args, 1);
            if (target == null) return;
            
            target.setWalkSpeed(speed);
            target.setFlySpeed(speed);
            
            sender.sendMessage("§aСкорость установлена на §6" + args[0] + " §aдля §6" + target.getName());
                
        } catch (NumberFormatException e) {
            sender.sendMessage("§cНеверная скорость. Используйте число от 1 до 10");
        }
    }
    
    private void handleKill(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.setHealth(0);
        
        if (target == sender) {
            target.sendMessage("§cВы совершили суицид!");
        } else {
            sender.sendMessage("§cИгрок §6" + target.getName() + " §cубит!");
        }
    }
    
    private void handleKick(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /кик <игрок> [причина]");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
            return;
        }
        
        String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "Не указана";
        
        target.kickPlayer("§cВы были кикнуты. Причина: §6" + reason);
        sender.sendMessage("§cИгрок §6" + target.getName() + " §cкикнут. Причина: §6" + reason);
    }
    
    private void handleMute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /мут <игрок>");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
            return;
        }
        
        boolean muted = !mutedPlayers.contains(target.getUniqueId());
        
        if (muted) {
            mutedPlayers.add(target.getUniqueId());
            sender.sendMessage("§cИгрок §6" + target.getName() + " §cзаглушен!");
        } else {
            mutedPlayers.remove(target.getUniqueId());
            sender.sendMessage("§aИгрок §6" + target.getName() + " §aразглушен!");
        }
    }
    
    private void handleClear(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.getInventory().clear();
        
        if (target == sender) {
            target.sendMessage("§aВаш инвентарь очищен!");
        } else {
            sender.sendMessage("§aИнвентарь игрока §6" + target.getName() + " §aочищен!");
            target.sendMessage("§aВаш инвентарь очищен!");
        }
    }
    
    private void handleBurn(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /огонь <игрок> [секунды]");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
            return;
        }
        
        int seconds = args.length > 1 ? Integer.parseInt(args[1]) : 10;
        target.setFireTicks(seconds * 20);
        
        sender.sendMessage("§cИгрок §6" + target.getName() + " §cподожжен на §6" + seconds + " §cсекунд!");
    }
    
    private void handleStrike(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /молния <игрок>");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
            return;
        }
        
        target.getWorld().strikeLightning(target.getLocation());
        sender.sendMessage("§cВ игрока §6" + target.getName() + " §cпопала молния!");
    }
    
    private void handleTime(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /время <day/night/noon/midnight/число>");
            return;
        }
        
        World world = sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().get(0);
        long time;
        
        switch (args[0].toLowerCase()) {
            case "day": case "день": time = 1000; break;
            case "night": case "ночь": time = 13000; break;
            case "noon": case "полдень": time = 6000; break;
            case "midnight": case "полночь": time = 18000; break;
            default:
                try {
                    time = Long.parseLong(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cНеверное время. Используйте: day, night, noon, midnight или число");
                    return;
                }
        }
        
        world.setTime(time);
        sender.sendMessage("§aВремя установлено на §6" + args[0]);
    }
    
    private void handleWeather(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /погода <clear/rain/thunder>");
            return;
        }
        
        World world = sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().get(0);
        
        switch (args[0].toLowerCase()) {
            case "clear": case "ясно":
                world.setStorm(false);
                world.setThundering(false);
                break;
            case "rain": case "дождь":
                world.setStorm(true);
                world.setThundering(false);
                break;
            case "thunder": case "гроза":
                world.setStorm(true);
                world.setThundering(true);
                break;
            default:
                sender.sendMessage("§cНеверная погода. Используйте: clear, rain, thunder");
                return;
        }
        
        sender.sendMessage("§aПогода установлена на §6" + args[0]);
    }
    
    private void handleInvsee(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /инвентарь <игрок>");
            return;
        }
        
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
            return;
        }
        
        player.openInventory(target.getInventory());
        sender.sendMessage("§aОткрыт инвентарь игрока §6" + target.getName());
    }
    
    private void handleWorkbench(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        player.openWorkbench(null, true);
        player.sendMessage("§aОткрыт верстак!");
    }
    
    private void handleEnderchest(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        player.openInventory(target.getEnderChest());
        player.sendMessage("§aОткрыт эндерсундук игрока §6" + target.getName());
    }
    
    private void handleTop(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        Location location = player.getLocation();
        Location highest = location.getWorld().getHighestBlockAt(location).getLocation();
        highest.setY(highest.getY() + 1);
        
        player.teleport(highest);
        player.sendMessage("§aТелепортирован на верхнюю точку!");
    }
    
    private void handleSkull(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /череп <игрок>");
            return;
        }
        
        Player player = (Player) sender;
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(args[0]));
        skull.setItemMeta(meta);
        
        player.getInventory().addItem(skull);
        player.sendMessage("§aВыдан череп игрока §6" + args[0]);
    }
    
    private void handleFlySpeed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /скоростьполета <1-10> [игрок]");
            return;
        }
        
        try {
            float speed = Float.parseFloat(args[0]) / 10f;
            if (speed < 0.1f || speed > 1.0f) {
                sender.sendMessage("§cНеверная скорость. Используйте число от 1 до 10");
                return;
            }
            
            Player target = getTarget(sender, args, 1);
            if (target == null) return;
            
            target.setFlySpeed(speed);
            sender.sendMessage("§aСкорость полета установлена на §6" + args[0] + " §aдля §6" + target.getName());
                
        } catch (NumberFormatException e) {
            sender.sendMessage("§cНеверная скорость. Используйте число от 1 до 10");
        }
    }
    
    private void handleWalkSpeed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /скоростьходьбы <1-10> [игрок]");
            return;
        }
        
        try {
            float speed = Float.parseFloat(args[0]) / 10f;
            if (speed < 0.1f || speed > 1.0f) {
                sender.sendMessage("§cНеверная скорость. Используйте число от 1 до 10");
                return;
            }
            
            Player target = getTarget(sender, args, 1);
            if (target == null) return;
            
            target.setWalkSpeed(speed);
            sender.sendMessage("§aСкорость ходьбы установлена на §6" + args[0] + " §aдля §6" + target.getName());
                
        } catch (NumberFormatException e) {
            sender.sendMessage("§cНеверная скорость. Используйте число от 1 до 10");
        }
    }
    
    private void handleHat(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
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
        player.sendMessage("§aПредмет надет на голову!");
    }
    
    private void handleBack(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        Location lastLocation = lastLocations.get(player.getUniqueId());
        
        if (lastLocation == null) {
            player.sendMessage("§cНет предыдущей локации!");
            return;
        }
        
        player.teleport(lastLocation);
        player.sendMessage("§aТелепортирован на предыдущую локацию!");
    }
    
    private void handleBroadcast(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /объявление <сообщение>");
            return;
        }
        
        String message = String.join(" ", args);
        Bukkit.broadcastMessage("§4[ОБЪЯВЛЕНИЕ] §6" + message);
    }
    
    private void handleSay(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /сказать <сообщение>");
            return;
        }
        
        String message = String.join(" ", args);
        Bukkit.broadcastMessage("§6[Сервер] §f" + message);
    }
    
    private void handleSudo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользование: /судо <игрок> <команда/сообщение>");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
            return;
        }
        
        String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        
        if (command.startsWith("/")) {
            target.performCommand(command.substring(1));
            sender.sendMessage("§aИгрок §6" + target.getName() + " §aвыполнил команду: §6" + command);
        } else {
            target.chat(command);
            sender.sendMessage("§aИгрок §6" + target.getName() + " §aсказал: §6" + command);
        }
    }
    
    private void handleRepair(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        boolean repairAll = args.length > 0 && "all".equalsIgnoreCase(args[0]);
        
        if (repairAll) {
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
                target.sendMessage("§aВсе предметы починены!");
            } else {
                sender.sendMessage("§aВсе предметы починены для §6" + target.getName());
                target.sendMessage("§aВсе предметы починены!");
            }
        } else {
            ItemStack hand = target.getInventory().getItemInMainHand();
            if (hand != null && hand.getType().getMaxDurability() > 0) {
                hand.setDurability((short) 0);
                
                if (target == sender) {
                    target.sendMessage("§aПредмет в руке починен!");
                } else {
                    sender.sendMessage("§aПредмет в руке починен для §6" + target.getName());
                    target.sendMessage("§aПредмет в руке починен!");
                }
            } else {
                sender.sendMessage("§cПредмет в руке нельзя починить!");
            }
        }
    }
    
    private void handleGamemode(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /режим <survival/creative/adventure/spectator> [игрок]");
            return;
        }
        
        GameMode gamemode = parseGamemode(args[0]);
        if (gamemode == null) {
            sender.sendMessage("§cНеверный режим игры. Доступные: survival, creative, adventure, spectator");
            return;
        }
        
        Player target = getTarget(sender, args, 1);
        if (target == null) return;
        
        target.setGameMode(gamemode);
        
        if (target == sender) {
            target.sendMessage("§aВаш режим игры изменен на §6" + gamemode.name().toLowerCase());
        } else {
            sender.sendMessage("§aРежим игры изменен на §6" + gamemode.name().toLowerCase() + " §aдля §6" + target.getName());
            target.sendMessage("§aВаш режим игры изменен на §6" + gamemode.name().toLowerCase());
        }
    }
    
    private void handleTeleport(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /телепорт <игрок/x y z>");
            return;
        }
        
        lastLocations.put(player.getUniqueId(), player.getLocation());
        
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer != null) {
            player.teleport(targetPlayer.getLocation());
            player.sendMessage("§aТелепортирован к §6" + targetPlayer.getName());
            return;
        }
        
        if (args.length >= 3) {
            try {
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);
                
                Location location = new Location(player.getWorld(), x, y, z);
                player.teleport(location);
                
                player.sendMessage("§aТелепортирован к координатам §6" + x + " " + y + " " + z);
                return;
            } catch (NumberFormatException e) {
                // Не координаты
            }
        }
        
        sender.sendMessage("§cИгрок §6" + args[0] + " §cне найден!");
    }
    
    // === НОВЫЕ КРУТЫЕ КОМАНДЫ ===
    private void handleSuperPower(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*60*5, 2, true, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20*60*5, 2, true, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*5, 1, true, true));
        
        target.sendMessage("§6Супер-сила активирована на 5 минут!");
        
        if (target != sender) {
            sender.sendMessage("§6Супер-сила дана игроку §e" + target.getName());
        }
    }
    
    private void handleLaser(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        Location start = player.getEyeLocation();
        Location end = start.clone().add(start.getDirection().multiply(50));
        
        double distance = start.distance(end);
        Vector direction = start.getDirection();
        
        for (double d = 0; d < distance; d += 0.5) {
            Location point = start.clone().add(direction.clone().multiply(d));
            player.getWorld().spawnParticle(Particle.DUST, point, 1, 
                new Particle.DustOptions(Color.RED, 1));
        }
        
        end.getWorld().createExplosion(end, 3.0f, false, true);
        player.sendMessage("§cЛазерный луч выпущен!");
    }
    
    private void handleTelekinesis(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20*30, 1, true, true));
        player.sendMessage("§dТелекинез активирован! Вы парите 30 секунд!");
    }
    
    private void handleTornado(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks++ > 200) {
                    cancel();
                    return;
                }
                
                for (double y = 0; y < 10; y += 0.5) {
                    for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 8) {
                        double x = Math.cos(angle + ticks * 0.1) * (y * 0.3);
                        double z = Math.sin(angle + ticks * 0.1) * (y * 0.3);
                        Location particleLoc = center.clone().add(x, y, z);
                        player.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 1);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        player.sendMessage("§7Торнадо создано!");
    }
    
    private void handleRainbow(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        
        new BukkitRunnable() {
            int ticks = 0;
            final Color[] rainbowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, 
                Color.BLUE, Color.fromRGB(75, 0, 130), Color.fromRGB(238, 130, 238)
            };
            
            @Override
            public void run() {
                if (ticks++ > 100 || !player.isOnline()) {
                    cancel();
                    return;
                }
                
                Color color = rainbowColors[ticks / 15 % rainbowColors.length];
                player.getWorld().spawnParticle(Particle.DUST, player.getLocation(), 10, 
                    0.5, 0.1, 0.5, new Particle.DustOptions(color, 2));
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        player.sendMessage("§dРадужный след активирован на 5 секунд!");
    }
    
    private void handleMeteor(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Location center = player.getLocation();
        
        new BukkitRunnable() {
            int meteors = 0;
            
            @Override
            public void run() {
                if (meteors++ > 20) {
                    cancel();
                    return;
                }
                
                double angle = 2 * Math.PI * meteors / 20;
                double x = center.getX() + 10 * Math.cos(angle);
                double z = center.getZ() + 10 * Math.sin(angle);
                Location meteorLoc = new Location(center.getWorld(), x, center.getY() + 20, z);
                
                center.getWorld().spawnParticle(Particle.FLAME, meteorLoc, 5);
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location explosionLoc = new Location(center.getWorld(), x, center.getY(), z);
                        center.getWorld().createExplosion(explosionLoc, 2.0f, false, true);
                    }
                }.runTaskLater(plugin, 20L);
            }
        }.runTaskTimer(plugin, 0L, 5L);
        
        player.sendMessage("§6Метеоритный дождь начался!");
    }
    
    private void handleFullRepair(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
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
        
        target.sendMessage("§aВсе предметы полностью починены!");
        
        if (target != sender) {
            sender.sendMessage("§aВсе предметы починены для §6" + target.getName());
        }
    }
    
    private void handleAntigravity(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭту команду могут использовать только игроки!");
            return;
        }
        
        Player player = (Player) sender;
        player.setGravity(!player.hasGravity());
        
        if (!player.hasGravity()) {
            player.sendMessage("§eАнтигравитация включена!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, Integer.MAX_VALUE, 0, true, false));
        } else {
            player.sendMessage("§eАнтигравитация выключена!");
            player.removePotionEffect(PotionEffectType.LEVITATION);
        }
    }
    
    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===
    private Player getTarget(CommandSender sender, String[] args, int argIndex) {
        if (args.length > argIndex) {
            Player target = Bukkit.getPlayer(args[argIndex]);
            if (target == null) {
                sender.sendMessage("§cИгрок §6" + args[argIndex] + " §cне найден!");
                return null;
            }
            return target;
        }
        
        if (sender instanceof Player) {
            return (Player) sender;
        }
        
        sender.sendMessage("§cЭту команду могут использовать только игроки!");
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
}

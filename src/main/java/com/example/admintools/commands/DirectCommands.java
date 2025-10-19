package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import com.example.admintools.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class DirectCommands implements CommandExecutor {
    
    private final AdminToolsPlugin plugin;
    private final ConfigManager configManager;
    private final AdminCommand adminCommand;
    
    public DirectCommands(AdminToolsPlugin plugin, AdminCommand adminCommand) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.adminCommand = adminCommand;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        
        switch (commandName) {
            case "рыба":
                giveMagicFish(sender, args);
                break;
            case "полет":
                adminCommand.handleFly(sender, args);
                break;
            case "исцелить":
                adminCommand.handleHeal(sender, args);
                break;
            case "еда":
                adminCommand.handleFeed(sender, args);
                break;
            case "бог":
                adminCommand.handleGod(sender, args);
                break;
            case "невидимость":
                adminCommand.handleVanish(sender, args);
                break;
            case "скорость":
                adminCommand.handleSpeed(sender, args);
                break;
            case "убить":
                adminCommand.handleKill(sender, args);
                break;
            case "кик":
                adminCommand.handleKick(sender, args);
                break;
            case "мут":
                adminCommand.handleMute(sender, args);
                break;
            case "очистить":
                adminCommand.handleClear(sender, args);
                break;
            case "огонь":
                adminCommand.handleBurn(sender, args);
                break;
            case "молния":
                adminCommand.handleStrike(sender, args);
                break;
            case "время":
                adminCommand.handleTime(sender, args);
                break;
            case "погода":
                adminCommand.handleWeather(sender, args);
                break;
            case "инвентарь":
                adminCommand.handleInvsee(sender, args);
                break;
            case "верстак":
                adminCommand.handleWorkbench(sender, args);
                break;
            case "эндсундук":
                adminCommand.handleEnderchest(sender, args);
                break;
            case "верх":
                adminCommand.handleTop(sender, args);
                break;
            case "череп":
                adminCommand.handleSkull(sender, args);
                break;
            case "скоростьполета":
                adminCommand.handleFlySpeed(sender, args);
                break;
            case "скоростьходьбы":
                adminCommand.handleWalkSpeed(sender, args);
                break;
            case "шляпа":
                adminCommand.handleHat(sender, args);
                break;
            case "назад":
                adminCommand.handleBack(sender, args);
                break;
            case "объявление":
                adminCommand.handleBroadcast(sender, args);
                break;
            case "сказать":
                adminCommand.handleSay(sender, args);
                break;
            case "судо":
                adminCommand.handleSudo(sender, args);
                break;
            case "починить":
                adminCommand.handleRepair(sender, args);
                break;
            case "режим":
                adminCommand.handleGamemode(sender, args);
                break;
            case "телепорт":
                adminCommand.handleTeleport(sender, args);
                break;
                
            // НОВЫЕ ПРОСТЫЕ КОМАНДЫ
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
                
            default:
                sender.sendMessage("§cНеизвестная команда: /" + commandName);
                break;
        }
        
        return true;
    }
    
    // === ВОЛШЕБНАЯ РЫБА ===
    private void giveMagicFish(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
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
    
    // === НОВЫЕ КОМАНДЫ ===
    private void handleSuperPower(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        // Используем существующие эффекты для версии 1.21
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
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Location start = player.getEyeLocation();
        Location end = start.clone().add(start.getDirection().multiply(50));
        
        // Создаем лазерный луч
        double distance = start.distance(end);
        Vector direction = start.getDirection();
        
        for (double d = 0; d < distance; d += 0.5) {
            Location point = start.clone().add(direction.clone().multiply(d));
            player.getWorld().spawnParticle(Particle.DUST, point, 1, 
                new Particle.DustOptions(Color.RED, 1));
        }
        
        // Взрыв в конце луча
        end.getWorld().createExplosion(end, 3.0f, false, true);
        player.sendMessage("§cЛазерный луч выпущен!");
    }
    
    private void handleTelekinesis(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20*30, 1, true, true));
        player.sendMessage("§dТелекинез активирован! Вы парите 30 секунд!");
    }
    
    private void handleTornado(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
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
            sender.sendMessage(configManager.getMessage("player-only"));
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
    
    private void handleAntigravity(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
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
}

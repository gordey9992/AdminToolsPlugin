package com.example.admintools.commands;

import com.example.admintools.AdminToolsPlugin;
import com.example.admintools.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

import java.util.*;

public class DirectCommands implements CommandExecutor {
    
    private final AdminToolsPlugin plugin;
    private final ConfigManager configManager;
    private final AdminCommand adminCommand;
    private final Set<UUID> superJumpPlayers = new HashSet<>();
    private final Map<UUID, Location> lastLocations = new HashMap<>();
    
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
            case "полныйремонт":
                handleFullRepair(sender, args);
                break;
            case "летающаярыба":
                giveFlyingFish(sender, args);
                break;
            case "ночноезрение":
                handleNightVision(sender, args);
                break;
            case "взрыв":
                handleExplosion(sender, args);
                break;
            case "полныйреген":
                handleFullRegen(sender, args);
                break;
            case "суперпрыжок":
                handleSuperJump(sender, args);
                break;
            case "клон":
                handleClone(sender, args);
                break;
            case "радуга":
                handleRainbow(sender, args);
                break;
            case "метеорит":
                handleMeteor(sender, args);
                break;
            case "инвиз":
                handleInvis(sender, args);
                break;
            case "полетрыбы":
                giveMagicFlyingFish(sender, args);
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
        meta.addEnchant(Enchantment.LUCK, 10, true);
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Магическая рыба, исполняющая желания!");
        lore.add("§7ПКМ - случайный эффект");
        lore.add("§7Shift+ПКМ - исцеление");
        lore.add("§7ПКМ по блоку - телепортация");
        lore.add("");
        lore.add("§6✨ Волшебная сила внутри!");
        meta.setLore(lore);
        
        fish.setItemMeta(meta);
        return fish;
    }
    
    private void giveMagicFlyingFish(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        ItemStack flyingFish = createFlyingFish();
        target.getInventory().addItem(flyingFish);
        
        target.sendMessage("§6✨ Вы получили §eЛетающую Волшебную Рыбу§6!");
        target.playSound(target.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_FLOP, 1.0f, 2.0f);
        
        if (target != sender) {
            sender.sendMessage("§aВыдали Летающую Рыбу игроку §6" + target.getName());
        }
    }
    
    private ItemStack createFlyingFish() {
        ItemStack fish = new ItemStack(Material.PUFFERFISH);
        ItemMeta meta = fish.getItemMeta();
        
        meta.setDisplayName("§eЛетающая Волшебная Рыба");
        meta.addEnchant(Enchantment.LOYALTY, 3, true);
        meta.addEnchant(Enchantment.RIPTIDE, 2, true);
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Рыба, которая умеет летать!");
        lore.add("§7ПКМ в воздухе - полет на 10 секунд");
        lore.add("§7Shift+ПКМ - ускорение");
        lore.add("§7ПКМ по воде - создать водоворот");
        lore.add("");
        lore.add("§e✈️ Взмывай в небеса!");
        meta.setLore(lore);
        
        fish.setItemMeta(meta);
        return fish;
    }
    
    private void giveFlyingFish(CommandSender sender, String[] args) {
        giveMagicFlyingFish(sender, args);
    }
    
    // === НОВЫЕ КОМАНДЫ ===
    private void handleNightVision(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*60*10, 0, true, true));
        target.sendMessage("§aНочное зрение включено на 10 минут!");
        
        if (target != sender) {
            sender.sendMessage("§aНочное зрение включено для §6" + target.getName());
        }
    }
    
    private void handleExplosion(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        float power = args.length > 0 ? Float.parseFloat(args[0]) : 4.0f;
        
        player.getWorld().createExplosion(player.getLocation(), power, false, true);
        player.sendMessage("§cСоздан взрыв силой " + power + "!");
    }
    
    private void handleFullRegen(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20);
        target.setSaturation(20);
        target.setFireTicks(0);
        
        // Добавляем регенерацию
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*30, 2, true, true));
        target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60, 4, true, true));
        
        target.sendMessage("§aПолное восстановление активировано!");
        
        if (target != sender) {
            sender.sendMessage("§aПолное восстановление для §6" + target.getName());
        }
    }
    
    private void handleSuperJump(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        boolean superJump = !superJumpPlayers.contains(target.getUniqueId());
        
        if (superJump) {
            superJumpPlayers.add(target.getUniqueId());
            AttributeInstance jumpAttribute = target.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);
            if (jumpAttribute != null) {
                jumpAttribute.setBaseValue(2.0); // Увеличенная высота прыжка
            }
            target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5, true, false));
            target.sendMessage("§aСупер-прыжок активирован!");
        } else {
            superJumpPlayers.remove(target.getUniqueId());
            AttributeInstance jumpAttribute = target.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);
            if (jumpAttribute != null) {
                jumpAttribute.setBaseValue(0.42); // Стандартная высота прыжка
            }
            target.removePotionEffect(PotionEffectType.JUMP);
            target.sendMessage("§cСупер-прыжок деактивирован!");
        }
        
        if (target != sender) {
            sender.sendMessage(superJump ? "§aСупер-прыжок активирован для §6" + target.getName() : 
                                          "§cСупер-прыжок деактивирован для §6" + target.getName());
        }
    }
    
    private void handleClone(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getMessage("player-only"));
            return;
        }
        
        Player player = (Player) sender;
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        // Создаем клона (арморстенд с головой игрока)
        Location cloneLoc = target.getLocation().add(1, 0, 0);
        target.getWorld().spawn(cloneLoc, org.bukkit.entity.ArmorStand.class, armorStand -> {
            armorStand.setVisible(true);
            armorStand.setGravity(false);
            armorStand.setCustomName("Клон " + target.getName());
            armorStand.setCustomNameVisible(true);
            
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            org.bukkit.inventory.meta.SkullMeta skullMeta = (org.bukkit.inventory.meta.SkullMeta) skull.getItemMeta();
            skullMeta.setOwningPlayer(target);
            skull.setItemMeta(skullMeta);
            
            armorStand.setHelmet(skull);
        });
        
        player.sendMessage("§aКлон игрока §6" + target.getName() + " §aсоздан!");
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
                
                // Создаем "метеорит"
                center.getWorld().spawnParticle(Particle.FLAME, meteorLoc, 5);
                center.getWorld().spawnParticle(Particle.SMOKE_LARGE, meteorLoc, 3);
                
                // Взрыв при "падении"
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
    
    private void handleInvis(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*60*5, 0, true, true));
        target.sendMessage("§7Вы стали невидимыми на 5 минут!");
        
        if (target != sender) {
            sender.sendMessage("§7Игрок §6" + target.getName() + " §7стал невидимым!");
        }
    }
    
    private void handleFullRepair(CommandSender sender, String[] args) {
        Player target = getTarget(sender, args, 0);
        if (target == null) return;
        
        // Чиним все предметы в инвентаре
        for (ItemStack item : target.getInventory().getContents()) {
            if (item != null && item.getType().getMaxDurability() > 0) {
                item.setDurability((short) 0);
            }
        }
        
        // Чиним броню
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
    
    // === ПЕРЕИСПОЛЬЗУЕМ МЕТОДЫ ИЗ AdminCommand ===
    private void handleFly(CommandSender sender, String[] args) {
        adminCommand.handleFly(sender, args);
    }
    
    private void handleHeal(CommandSender sender, String[] args) {
        adminCommand.handleHeal(sender, args);
    }
    
    private void handleFeed(CommandSender sender, String[] args) {
        adminCommand.handleFeed(sender, args);
    }
    
    private void handleGod(CommandSender sender, String[] args) {
        adminCommand.handleGod(sender, args);
    }
    
    private void handleVanish(CommandSender sender, String[] args) {
        adminCommand.handleVanish(sender, args);
    }
    
    private void handleSpeed(CommandSender sender, String[] args) {
        adminCommand.handleSpeed(sender, args);
    }
    
    private void handleKill(CommandSender sender, String[] args) {
        adminCommand.handleKill(sender, args);
    }
    
    private void handleKick(CommandSender sender, String[] args) {
        adminCommand.handleKick(sender, args);
    }
    
    private void handleMute(CommandSender sender, String[] args) {
        adminCommand.handleMute(sender, args);
    }
    
    private void handleClear(CommandSender sender, String[] args) {
        adminCommand.handleClear(sender, args);
    }
    
    private void handleBurn(CommandSender sender, String[] args) {
        adminCommand.handleBurn(sender, args);
    }
    
    private void handleStrike(CommandSender sender, String[] args) {
        adminCommand.handleStrike(sender, args);
    }
    
    private void handleTime(CommandSender sender, String[] args) {
        adminCommand.handleTime(sender, args);
    }
    
    private void handleWeather(CommandSender sender, String[] args) {
        adminCommand.handleWeather(sender, args);
    }
    
    private void handleInvsee(CommandSender sender, String[] args) {
        adminCommand.handleInvsee(sender, args);
    }
    
    private void handleWorkbench(CommandSender sender, String[] args) {
        adminCommand.handleWorkbench(sender, args);
    }
    
    private void handleEnderchest(CommandSender sender, String[] args) {
        adminCommand.handleEnderchest(sender, args);
    }
    
    private void handleTop(CommandSender sender, String[] args) {
        adminCommand.handleTop(sender, args);
    }
    
    private void handleSkull(CommandSender sender, String[] args) {
        adminCommand.handleSkull(sender, args);
    }
    
    private void handleFlySpeed(CommandSender sender, String[] args) {
        adminCommand.handleFlySpeed(sender, args);
    }
    
    private void handleWalkSpeed(CommandSender sender, String[] args) {
        adminCommand.handleWalkSpeed(sender, args);
    }
    
    private void handleHat(CommandSender sender, String[] args) {
        adminCommand.handleHat(sender, args);
    }
    
    private void handleBack(CommandSender sender, String[] args) {
        adminCommand.handleBack(sender, args);
    }
    
    private void handleBroadcast(CommandSender sender, String[] args) {
        adminCommand.handleBroadcast(sender, args);
    }
    
    private void handleSay(CommandSender sender, String[] args) {
        adminCommand.handleSay(sender, args);
    }
    
    private void handleSudo(CommandSender sender, String[] args) {
        adminCommand.handleSudo(sender, args);
    }
    
    private void handleRepair(CommandSender sender, String[] args) {
        adminCommand.handleRepair(sender, args);
    }
    
    private void handleGamemode(CommandSender sender, String[] args) {
        adminCommand.handleGamemode(sender, args);
    }
    
    private void handleTeleport(CommandSender sender, String[] args) {
        adminCommand.handleTeleport(sender, args);
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

    // === ЕЩЕ БОЛЬШЕ КРУТЫХ КОМАНД ===

private void handleSuperPower(CommandSender sender, String[] args) {
    Player target = getTarget(sender, args, 0);
    if (target == null) return;
    
    target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*5, 2, true, true));
    target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*5, 2, true, true));
    target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*5, 1, true, true));
    
    target.sendMessage("§6Супер-сила активирована на 5 минут!");
    
    if (target != sender) {
        sender.sendMessage("§6Супер-сила дана игроку §e" + target.getName());
    }
}

private void handleWaterfall(CommandSender sender, String[] args) {
    if (!(sender instanceof Player)) {
        sender.sendMessage(configManager.getMessage("player-only"));
        return;
    }
    
    Player player = (Player) sender;
    Location loc = player.getLocation();
    
    new BukkitRunnable() {
        int ticks = 0;
        @Override
        public void run() {
            if (ticks++ > 100) {
                cancel();
                return;
            }
            
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    Location waterLoc = loc.clone().add(x, 5, z);
                    player.getWorld().spawnParticle(Particle.WATER_SPLASH, waterLoc, 2);
                }
            }
        }
    }.runTaskTimer(plugin, 0L, 2L);
    
    player.sendMessage("§9Водопад создан!");
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
        player.getWorld().spawnParticle(Particle.REDSTONE, point, 1, 
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

private void handleFreeze(CommandSender sender, String[] args) {
    Player target = getTarget(sender, args, 0);
    if (target == null) return;
    
    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20*30, 255, true, true));
    target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*30, 128, true, true));
    
    // Ледяные частицы
    new BukkitRunnable() {
        int ticks = 0;
        @Override
        public void run() {
            if (ticks++ > 600 || !target.isOnline()) {
                cancel();
                return;
            }
            target.getWorld().spawnParticle(Particle.SNOW_SHOVEL, target.getLocation(), 5);
        }
    }.runTaskTimer(plugin, 0L, 5L);
    
    target.sendMessage("§bВы заморожены на 30 секунд!");
    
    if (target != sender) {
        sender.sendMessage("§bИгрок §3" + target.getName() + " §bзаморожен!");
    }
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

private void handleEarthquake(CommandSender sender, String[] args) {
    if (!(sender instanceof Player)) {
        sender.sendMessage(configManager.getMessage("player-only"));
        return;
    }
    
    Player player = (Player) sender;
    Location center = player.getLocation();
    
    // Тряска камеры у всех игроков поблизости
    for (Player nearby : player.getWorld().getPlayers()) {
        if (nearby.getLocation().distance(center) < 20) {
            nearby.playEffect(EntityEffect.TOTEM_RESURRECT);
        }
    }
    
    // Взрывы вокруг
    for (int i = 0; i < 8; i++) {
        double angle = 2 * Math.PI * i / 8;
        double x = center.getX() + 5 * Math.cos(angle);
        double z = center.getZ() + 5 * Math.sin(angle);
        Location explosionLoc = new Location(center.getWorld(), x, center.getY(), z);
        center.getWorld().createExplosion(explosionLoc, 2.0f, false, true);
    }
    
    player.sendMessage("§6Землетрясение началось!");
}

private void handleNectar(CommandSender sender, String[] args) {
    Player target = getTarget(sender, args, 0);
    if (target == null) return;
    
    target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*60, 2, true, true));
    target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60, 4, true, true));
    target.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 20*60, 1, true, true));
    
    // Цветочные частицы
    new BukkitRunnable() {
        int ticks = 0;
        @Override
        public void run() {
            if (ticks++ > 100) {
                cancel();
                return;
            }
            target.getWorld().spawnParticle(Particle.HEART, target.getLocation().add(0, 2, 0), 2);
        }
    }.runTaskTimer(plugin, 0L, 10L);
    
    target.sendMessage("§dНектар богов выпит! Удача на вашей стороне!");
    
    if (target != sender) {
        sender.sendMessage("§dНектар дан игроку §5" + target.getName());
    }
}

private void handleRadiation(CommandSender sender, String[] args) {
    Player target = getTarget(sender, args, 0);
    if (target == null) return;
    
    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*30, 1, true, true));
    target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*15, 0, true, true));
    
    // Радиоактивные частицы
    new BukkitRunnable() {
        int ticks = 0;
        @Override
        public void run() {
            if (ticks++ > 600 || !target.isOnline()) {
                cancel();
                return;
            }
            target.getWorld().spawnParticle(Particle.SLIME, target.getLocation(), 3);
        }
    }.runTaskTimer(plugin, 0L, 5L);
    
    target.sendMessage("§a☢ Вы подверглись радиации!");
    
    if (target != sender) {
        sender.sendMessage("§aРадиация применена к §2" + target.getName());
    }
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

// === ДОБАВЛЯЕМ ВЫЗОВЫ НОВЫХ КОМАНД В SWITCH ===
// Этот код должен быть внутри метода onCommand, в блоке switch

// Добавьте эти case в существующий switch в методе onCommand:
/*
case "суперсила":
    handleSuperPower(sender, args);
    break;
case "водопад":
    handleWaterfall(sender, args);
    break;
case "лазер":
    handleLaser(sender, args);
    break;
case "телекинез":
    handleTelekinesis(sender, args);
    break;
case "заморозка":
    handleFreeze(sender, args);
    break;
case "торнадо":
    handleTornado(sender, args);
    break;
case "землетрясение":
    handleEarthquake(sender, args);
    break;
case "нектар":
    handleNectar(sender, args);
    break;
case "радиация":
    handleRadiation(sender, args);
    break;
case "антигравитация":
    handleAntigravity(sender, args);
    break;
*/

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
} // <-- ЗАКРЫВАЮЩАЯ СКОБКА КЛАССА DirectCommands

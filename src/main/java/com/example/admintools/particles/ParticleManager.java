package com.example.admintools.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.example.admintools.AdminToolsPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticleManager {
    
    private final AdminToolsPlugin plugin;
    private final Map<UUID, ParticleTask> activeParticles = new HashMap<>();
    
    public ParticleManager(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setParticleTrail(Player player, String particleType, String color, double size) {
        // Останавливаем существующие частицы
        disableParticleTrail(player);
        
        Particle particle;
        Object data = null;
        
        try {
            particle = Particle.valueOf(particleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Если частица не найдена, используем стандартную
            particle = Particle.FLAME;
        }
        
        // Обработка цвета для DUST_COLOR_TRANSITION
        if (particle == Particle.DUST_COLOR_TRANSITION && color != null) {
            try {
                Color fromColor = parseColor(color);
                Color toColor = fromColor; // Можно настроить переход
                data = new Particle.DustTransition(fromColor, toColor, (float) size);
            } catch (Exception e) {
                data = new Particle.DustTransition(Color.RED, Color.ORANGE, (float) size);
            }
        }
        // Обработка цвета для DUST
        else if (particle == Particle.DUST && color != null) {
            try {
                Color particleColor = parseColor(color);
                data = new Particle.DustOptions(particleColor, (float) size);
            } catch (Exception e) {
                data = new Particle.DustOptions(Color.RED, (float) size);
            }
        }
        
        ParticleTask task = new ParticleTask(player, particle, data);
        task.runTaskTimer(plugin, 0L, 1L); // Каждый тик
        
        activeParticles.put(player.getUniqueId(), task);
    }
    
    public void disableParticleTrail(Player player) {
        ParticleTask task = activeParticles.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }
    
    public void disableAllParticles() {
        for (ParticleTask task : activeParticles.values()) {
            task.cancel();
        }
        activeParticles.clear();
    }
    
    public boolean hasParticleTrail(Player player) {
        return activeParticles.containsKey(player.getUniqueId());
    }
    
    private Color parseColor(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        
        int rgb = Integer.parseInt(hexColor, 16);
        return Color.fromRGB(rgb);
    }
    
    public String getAvailableParticles() {
        StringBuilder sb = new StringBuilder();
        Particle[] particles = Particle.values();
        
        for (int i = 0; i < Math.min(particles.length, 20); i++) {
            sb.append(particles[i].name().toLowerCase());
            if (i < Math.min(particles.length, 20) - 1) {
                sb.append(", ");
            }
        }
        
        if (particles.length > 20) {
            sb.append("... (всего ").append(particles.length).append(")");
        }
        
        return sb.toString();
    }
}

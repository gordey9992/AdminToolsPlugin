package com.example.admintools.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleTask extends BukkitRunnable {
    
    private final Player player;
    private final Particle particle;
    private final Object data;
    
    public ParticleTask(Player player, Particle particle, Object data) {
        this.player = player;
        this.particle = particle;
        this.data = data;
    }
    
    @Override
    public void run() {
        if (!player.isOnline() || player.isDead()) {
            cancel();
            return;
        }
        
        Location location = player.getLocation().clone();
        location.add(0, 0.1, 0); // Немного выше ног
        
        // Создаем частицы вокруг игрока (видно всем)
        if (data != null) {
            player.getWorld().spawnParticle(particle, location, 3, 0.2, 0.1, 0.2, 0.02, data);
        } else {
            player.getWorld().spawnParticle(particle, location, 3, 0.2, 0.1, 0.2, 0.02);
        }
    }
}

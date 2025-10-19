package com.example.admintools.chat;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class ChatListener implements Listener {
    
    private final AdminToolsPlugin plugin;
    
    public ChatListener(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Проверяем включен ли телеграм
        if (!plugin.getConfigManager().getConfig().getBoolean("telegram.enabled", false)) {
            return;
        }
        
        String message = event.getMessage();
        String playerName = event.getPlayer().getName();
        
        // Проверяем игнорируемые префиксы
        List<String> ignorePrefixes = plugin.getConfigManager().getConfig().getStringList("telegram.ignore-prefixes");
        for (String prefix : ignorePrefixes) {
            if (message.startsWith(prefix)) {
                return;
            }
        }
        
        // Для EssentialsXChat проверяем канал по формату сообщения
        // EssentialsXChat обычно форматирует сообщения как [Канал] Игрок: сообщение
        // Или использует разные форматы для разных каналов
        
        // Проверяем локальные каналы по ключевым словам в сообщении
        List<String> ignoreChannels = plugin.getConfigManager().getConfig().getStringList("telegram.ignore-channels");
        String lowerMessage = message.toLowerCase();
        
        for (String channel : ignoreChannels) {
            // Если сообщение содержит указание на локальный чат
            if (lowerMessage.contains("!") || 
                lowerMessage.contains("[local]") || 
                lowerMessage.contains("[локальный]") ||
                lowerMessage.contains("[l]") ||
                event.getFormat().toLowerCase().contains("local") ||
                event.getFormat().toLowerCase().contains("локальный")) {
                return;
            }
        }
        
        // Проверяем формат сообщения на наличие указаний локального чата
        String format = event.getFormat().toLowerCase();
        if (format.contains("local") || format.contains("локальный") || format.contains("[l]")) {
            return;
        }
        
        // Если прошли все проверки - отправляем в телеграм
        plugin.getTelegramManager().sendToTelegram(playerName, message);
    }
}

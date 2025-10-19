package com.example.admintools.chat;

import com.example.admintools.AdminToolsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramManager {
    
    private final AdminToolsPlugin plugin;
    private final String botToken;
    private final String chatId;
    private int lastMessageId = 0;
    private boolean running = false;
    
    public TelegramManager(AdminToolsPlugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfigManager().getConfig();
        this.botToken = config.getString("telegram.bot-token");
        this.chatId = config.getString("telegram.chat-id");
    }
    
    public void startBot() {
        running = true;
        startUpdateChecker();
        plugin.getLogger().info("Telegram бот запущен!");
    }
    
    public void stopBot() {
        running = false;
        plugin.getLogger().info("Telegram бот остановлен!");
    }
    
    public void sendToTelegram(String playerName, String message) {
        if (!running) return;
        
        String format = plugin.getConfigManager().getConfig().getString("formats.minecraft-to-telegram");
        String formattedMessage = format.replace("{player}", playerName).replace("{message}", message);
        
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String urlString = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                    botToken,
                    chatId,
                    URLEncoder.encode(formattedMessage, StandardCharsets.UTF_8)
                );
                
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    plugin.getLogger().warning("Ошибка отправки сообщения в Telegram: " + responseCode);
                }
                
                conn.disconnect();
            } catch (Exception e) {
                plugin.getLogger().warning("Ошибка при отправке сообщения в Telegram: " + e.getMessage());
            }
        });
    }
    
    private void startUpdateChecker() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (!running) return;
            
            try {
                String urlString = String.format(
                    "https://api.telegram.org/bot%s/getUpdates?offset=%d",
                    botToken,
                    lastMessageId + 1
                );
                
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                processUpdates(response.toString());
                
            } catch (Exception e) {
                // Игнорируем ошибки, чтобы не спамить в консоль
            }
        }, 0L, 20L * plugin.getConfigManager().getConfig().getInt("telegram.update-interval", 2));
    }
    
    private void processUpdates(String jsonResponse) {
        // Простая обработка JSON (для простоты)
        if (!jsonResponse.contains("\"result\"")) return;
        
        String[] messages = jsonResponse.split("\"message\"");
        for (int i = 1; i < messages.length; i++) {
            try {
                String message = messages[i];
                
                // Получаем ID сообщения
                int messageId = Integer.parseInt(message.split("\"message_id\":")[1].split(",")[0].trim());
                if (messageId <= lastMessageId) continue;
                
                lastMessageId = messageId;
                
                // Проверяем что сообщение из нужного чата
                if (!message.contains("\"chat\":{\"id\":" + chatId)) continue;
                
                // Получаем текст сообщения
                if (!message.contains("\"text\"")) continue;
                
                String text = message.split("\"text\":\"")[1].split("\"")[0];
                
                // Получаем имя пользователя
                String username = "Неизвестно";
                if (message.contains("\"username\"")) {
                    username = message.split("\"username\":\"")[1].split("\"")[0];
                } else if (message.contains("\"first_name\"")) {
                    username = message.split("\"first_name\":\"")[1].split("\"")[0];
                }
                
                // Отправляем в Minecraft
                final String finalUsername = username;
                final String finalText = text;
                
                Bukkit.getScheduler().runTask(plugin, () -> {
                    String format = plugin.getConfigManager().getConfig().getString("formats.telegram-to-minecraft");
                    String minecraftMessage = format.replace("{username}", finalUsername).replace("{message}", finalText);
                    
                    Bukkit.broadcastMessage(minecraftMessage.replace("&", "§"));
                });
                
            } catch (Exception e) {
                // Игнорируем ошибки парсинга
            }
        }
    }
}

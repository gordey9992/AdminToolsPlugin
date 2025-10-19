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

public class TelegramManager { // <-- ДОБАВЬТЕ ЭТУ СТРОЧКУ!
    
    private final AdminToolsPlugin plugin;
    private final String botToken;
    private final String chatId;
    private final String topicId;
    private int lastMessageId = 0;
    private boolean running = false;
    
    public TelegramManager(AdminToolsPlugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfigManager().getConfig();
        this.botToken = config.getString("telegram.bot-token");
        this.chatId = config.getString("telegram.chat-id");
        this.topicId = config.getString("telegram.topic-id", "434");
    }
    
    public void startBot() {
        running = true;
        startUpdateChecker();
        plugin.getLogger().info("Telegram бот запущен! Топик: " + topicId);
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
                // Формируем URL с указанием topic_id для отправки в конкретную тему
                String urlString = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&message_thread_id=%s&text=%s",
                    botToken,
                    chatId,
                    topicId,
                    URLEncoder.encode(formattedMessage, StandardCharsets.UTF_8)
                );
                
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    // Читаем ошибку для отладки
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    errorReader.close();
                    
                    plugin.getLogger().warning("Ошибка отправки сообщения в Telegram: " + responseCode);
                    plugin.getLogger().warning("Ответ ошибки: " + errorResponse.toString());
                } else {
                    plugin.getLogger().info("Сообщение отправлено в топик " + topicId);
                }
                
                conn.disconnect();
            } catch (Exception e) {
                plugin.getLogger().warning("Ошибка при отправке сообщения в Telegram: " + e.getMessage());
                e.printStackTrace();
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
                
                // Проверяем что сообщение из нужной темы (topic)
                if (!message.contains("\"message_thread_id\":" + topicId)) {
                    continue;
                }
                
                // Получаем текст сообщения
                if (!message.contains("\"text\"")) continue;
                
                String text = message.split("\"text\":\"")[1].split("\"")[0];
                
                // Декодируем Unicode escape последовательности
                text = decodeUnicode(text);
                
                // Пропускаем сообщения от бота или системные
                if (text.startsWith("/") || text.contains("🎮")) {
                    continue;
                }
                
                // Получаем имя пользователя
                String username = "Неизвестно";
                if (message.contains("\"username\"")) {
                    username = message.split("\"username\":\"")[1].split("\"")[0];
                    username = decodeUnicode(username);
                } else if (message.contains("\"first_name\"")) {
                    username = message.split("\"first_name\":\"")[1].split("\"")[0];
                    username = decodeUnicode(username);
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
                plugin.getLogger().warning("Ошибка обработки сообщения из Telegram: " + e.getMessage());
            }
        }
    }
    
    // Метод для декодирования Unicode escape последовательностей
    private String decodeUnicode(String unicodeString) {
        try {
            // Декодируем Unicode escape sequences (\u0430 -> "а")
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < unicodeString.length()) {
                if (unicodeString.charAt(i) == '\\' && i + 1 < unicodeString.length() && unicodeString.charAt(i + 1) == 'u') {
                    // Нашли Unicode escape sequence
                    String hex = unicodeString.substring(i + 2, i + 6);
                    try {
                        int codePoint = Integer.parseInt(hex, 16);
                        sb.append((char) codePoint);
                        i += 6;
                    } catch (NumberFormatException e) {
                        sb.append(unicodeString.charAt(i));
                        i++;
                    }
                } else {
                    sb.append(unicodeString.charAt(i));
                    i++;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return unicodeString;
        }
    }
    
    public void debugChatInfo() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String urlString = String.format(
                    "https://api.telegram.org/bot%s/getUpdates",
                    botToken
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
                
                plugin.getLogger().info("Debug response: " + response.toString());
                conn.disconnect();
                
            } catch (Exception e) {
                plugin.getLogger().warning("Debug error: " + e.getMessage());
            }
        });
    }
} // <-- И ЭТА СКОБКА ДОЛЖНА БЫТЬ В КОНЦЕ ФАЙЛА!

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

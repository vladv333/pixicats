package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfig {
    private static final String CONFIG_FILE = "bot.properties";
    private final Properties properties;

    public BotConfig() {
        properties = new Properties();

        // First, try to load from environment variables (for production)
        String envToken = System.getenv("BOT_TOKEN");
        String envUsername = System.getenv("BOT_USERNAME");
        String envDesignerId = System.getenv("DESIGNER_CHAT_ID");

        if (envToken != null && !envToken.isEmpty()) {
            // Use environment variables (production)
            properties.setProperty("bot.token", envToken);
            properties.setProperty("bot.username", envUsername != null ? envUsername : "PixiCatsBot");
            properties.setProperty("designer.chat.id", envDesignerId != null ? envDesignerId : "");
            System.out.println("✅ Configuration loaded from environment variables");
        } else {
            // Try to load from file system (local development)
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                properties.load(fis);
                System.out.println("✅ Configuration loaded from: " + CONFIG_FILE);
            } catch (IOException e) {
                // Try to load from classpath (resources folder)
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                    if (is != null) {
                        properties.load(is);
                        System.out.println("✅ Configuration loaded from classpath");
                    } else {
                        throw new RuntimeException(
                                "Configuration not found!\n" +
                                        "Please either:\n" +
                                        "1. Set environment variables: BOT_TOKEN, BOT_USERNAME, DESIGNER_CHAT_ID\n" +
                                        "2. Create 'bot.properties' file in project root or src/main/resources/"
                        );
                    }
                } catch (IOException ex) {
                    throw new RuntimeException("Failed to load configuration", ex);
                }
            }
        }

        validateConfig();
    }

    private void validateConfig() {
        String token = getBotToken();
        if (token == null || token.equals("YOUR_BOT_TOKEN_HERE") || token.trim().isEmpty()) {
            throw new RuntimeException(
                    "❌ Bot token is not configured!\n" +
                            "Please set 'bot.token' in bot.properties file"
            );
        }
    }

    public String getBotToken() {
        return properties.getProperty("bot.token");
    }

    public String getBotUsername() {
        return properties.getProperty("bot.username", "PixiCatsBot");
    }

    public String getDesignerChatId() {
        return properties.getProperty("designer.chat.id", "");
    }
}
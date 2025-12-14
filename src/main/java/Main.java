import config.BotConfig;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class    Main {
    public static void main(String[] args) {
        try {
            System.out.println("🐱 Starting PixiCats Bot...");

            BotConfig config = new BotConfig();
            PixiCatsBot bot = new PixiCatsBot(config);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);

            System.out.println("✅ PixiCats Bot is running!");
            System.out.println("Bot username: " + config.getBotUsername());

        } catch (TelegramApiException e) {
            System.err.println("❌ Failed to start bot:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error:");
            e.printStackTrace();
        }
    }
}
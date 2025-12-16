import config.BotConfig;
import localization.Localization;
import model.StickerPack;
import model.User;
import utils.KeyboardBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PixiCatsBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, String> awaitingInput = new HashMap<>();

    public PixiCatsBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            handlePhotoMessage(update.getMessage());
        } else if (update.hasMessage() && update.getMessage().hasSuccessfulPayment()) {
            handleSuccessfulPayment(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasPreCheckoutQuery()) {
            handlePreCheckoutQuery(update.getPreCheckoutQuery());
        }
    }

    private void handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        User user = getOrCreateUser(chatId);

        // Check if user is in input mode
        String inputMode = awaitingInput.get(chatId);
        if (inputMode != null) {
            handleUserInput(chatId, text, inputMode, user.getLanguage());
            return;
        }

        if (text.equals("/start")) {
            sendMainMenu(chatId, user.getLanguage());
        } else if (text.equals("/myid")) {
            SendMessage idMessage = new SendMessage();
            idMessage.setChatId(chatId.toString());
            idMessage.setText("Your chat ID: " + chatId);
            try {
                execute(idMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePhotoMessage(Message message) {
        Long chatId = message.getChatId();
        String inputMode = awaitingInput.get(chatId);

        if (inputMode != null && (inputMode.equals("free") || inputMode.equals("extended"))) {
            // Handle photo upload for free or extended pack
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText("📸 Photo received! Send more or type 'done' when finished.");
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUserInput(Long chatId, String text, String mode, String lang) {
        String adminId = config.getDesignerChatId();

        switch (mode) {
            case "free" -> {
                if (text.equalsIgnoreCase("done")) {
                    awaitingInput.remove(chatId);
                    sendFreeConfirmation(chatId, lang);
                    // Notify admin
                    notifyAdmin(chatId, "FREE pack request", text);
                }
            }
            case "extended" -> {
                if (text.equalsIgnoreCase("done")) {
                    awaitingInput.remove(chatId);
                    sendExtendedConfirmation(chatId, lang);
                    // Notify admin
                    notifyAdmin(chatId, "EXTENDED pack (5€) request", text);
                }
            }
            case "exclusive" -> {
                awaitingInput.remove(chatId);
                sendExclusiveConfirmation(chatId, lang);
                // Send request to designer
                notifyAdmin(chatId, "EXCLUSIVE pack (15€) request", text);
            }
            case "support" -> {
                awaitingInput.remove(chatId);
                sendSupportConfirmation(chatId, lang);
                // Forward to admin
                notifyAdmin(chatId, "SUPPORT message", text);
            }
        }
    }

    private void notifyAdmin(Long userId, String type, String message) {
        String adminId = config.getDesignerChatId();
        if (adminId != null && !adminId.isEmpty()) {
            SendMessage toAdmin = new SendMessage();
            toAdmin.setChatId(adminId);
            toAdmin.setText("📬 " + type + " from user " + userId + ":\n\n" + message);
            try {
                execute(toAdmin);
            } catch (TelegramApiException e) {
                System.err.println("❌ Failed to notify admin: " + e.getMessage());
            }
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();
        String callbackId = callbackQuery.getId();

        User user = getOrCreateUser(chatId);
        String lang = user.getLanguage();

        // Handle add to cart separately
        if (data.startsWith("add_")) {
            handleAddToCart(chatId, callbackId, data.substring(4), user, lang);
            return;
        }

        // Answer callback for other actions
        answerCallback(callbackId);

        switch (data) {
            case "menu" -> editMainMenu(chatId, messageId, lang);
            case "free" -> editFreeMenu(chatId, messageId, lang);
            case "extended" -> editExtendedMenu(chatId, messageId, lang);
            case "exclusive" -> editExclusiveMenu(chatId, messageId, lang);
            case "store" -> editStoreMenu(chatId, messageId, lang);
            case "support" -> editSupportMenu(chatId, messageId, lang);
            case "settings" -> editSettingsMenu(chatId, messageId, lang);
            case "language" -> editLanguageMenu(chatId, messageId);
            case "cart" -> editCartMenu(chatId, messageId, user);
            case "checkout" -> handleCheckout(chatId, messageId, user);
            case "clear_cart" -> handleClearCart(chatId, messageId, user);
            case "pay" -> handlePayment(chatId, messageId, lang);
            default -> {
                if (data.startsWith("lang_")) {
                    handleLanguageChange(chatId, messageId, data.substring(5), user);
                } else if (data.startsWith("pack_")) {
                    editPackDetails(chatId, messageId, data, lang);
                }
            }
        }
    }

    // MAIN MENU
    private void sendMainMenu(Long chatId, String language) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("welcome", language));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(language));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editMainMenu(Long chatId, Integer messageId, String language) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("welcome", language));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(language));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendMainMenu(chatId, language);
        }
    }

    // FREE PACK
    private void editFreeMenu(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("free_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));

        awaitingInput.put(chatId, "free");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendFreeMenu(chatId, lang);
        }
    }

    private void sendFreeMenu(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("free_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));
        awaitingInput.put(chatId, "free");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendFreeConfirmation(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("free_received", lang));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(lang));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // EXTENDED PACK
    private void editExtendedMenu(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("extended_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));

        awaitingInput.put(chatId, "extended");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendExtendedMenu(chatId, lang);
        }
    }

    private void sendExtendedMenu(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("extended_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));
        awaitingInput.put(chatId, "extended");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendExtendedConfirmation(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("extended_received", lang));
        message.setReplyMarkup(KeyboardBuilder.buildPaymentMenu(lang, "btn_pay"));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // EXCLUSIVE PACK
    private void editExclusiveMenu(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("exclusive_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));

        awaitingInput.put(chatId, "exclusive");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendExclusiveMenu(chatId, lang);
        }
    }

    private void sendExclusiveMenu(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("exclusive_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));
        awaitingInput.put(chatId, "exclusive");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendExclusiveConfirmation(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("exclusive_received", lang));
        message.setReplyMarkup(KeyboardBuilder.buildPaymentMenu(lang, "btn_pay_exclusive"));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // SUPPORT
    private void editSupportMenu(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("support_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));

        awaitingInput.put(chatId, "support");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendSupportMenu(chatId, lang);
        }
    }

    private void sendSupportMenu(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("support_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildBackButton(lang));
        awaitingInput.put(chatId, "support");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendSupportConfirmation(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("support_received", lang));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(lang));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // STORE
    private void editStoreMenu(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("store_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildShopMenu(lang));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendStoreMenu(chatId, lang);
        }
    }

    private void sendStoreMenu(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("store_title", lang));
        message.setReplyMarkup(KeyboardBuilder.buildShopMenu(lang));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editPackDetails(Long chatId, Integer messageId, String packId, String lang) {
        StickerPack pack = StickerPack.getById(packId);
        if (pack == null) return;

        try {
            InputMediaPhoto photo = new InputMediaPhoto();
            photo.setMedia(pack.getPreviewUrl());
            photo.setCaption(pack.getName(lang) + "\n\n" +
                    String.format(Localization.get("pack_details", lang), pack.getPrice()));

            EditMessageMedia editMedia = new EditMessageMedia();
            editMedia.setChatId(chatId.toString());
            editMedia.setMessageId(messageId);
            editMedia.setMedia(photo);
            editMedia.setReplyMarkup(KeyboardBuilder.buildPackMenu(packId, lang));

            execute(editMedia);
        } catch (TelegramApiException e) {
            sendPackDetailsWithPhoto(chatId, packId, lang);
        }
    }

    private void sendPackDetailsWithPhoto(Long chatId, String packId, String lang) {
        StickerPack pack = StickerPack.getById(packId);
        if (pack == null) return;

        try {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(chatId.toString());
            photo.setPhoto(new InputFile(pack.getPreviewUrl()));
            photo.setCaption(pack.getName(lang) + "\n\n" +
                    String.format(Localization.get("pack_details", lang), pack.getPrice()));
            photo.setReplyMarkup(KeyboardBuilder.buildPackMenu(packId, lang));

            execute(photo);
        } catch (TelegramApiException e) {
            sendPackDetailsAsText(chatId, packId, lang);
        }
    }

    private void sendPackDetailsAsText(Long chatId, String packId, String lang) {
        StickerPack pack = StickerPack.getById(packId);
        if (pack == null) return;

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(pack.getName(lang) + "\n\n" +
                String.format(Localization.get("pack_details", lang), pack.getPrice()) +
                "\n\n🖼️ Preview image temporarily unavailable");
        message.setReplyMarkup(KeyboardBuilder.buildPackMenu(packId, lang));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // CART
    private void handleAddToCart(Long chatId, String callbackId, String packId, User user, String lang) {
        user.getCart().addItem(packId);

        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(Localization.get("added_to_cart", lang));
        answer.setShowAlert(false);

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editCartMenu(Long chatId, Integer messageId, User user) {
        String lang = user.getLanguage();
        StringBuilder text = new StringBuilder();

        if (user.getCart().isEmpty()) {
            text.append(Localization.get("cart_empty", lang));
        } else {
            text.append(Localization.get("cart_items", lang));
            int index = 1;
            for (String item : user.getCart().getItems()) {
                StickerPack pack = StickerPack.getById(item);
                if (pack != null) {
                    text.append(index++).append(". ").append(pack.getName(lang))
                            .append(" - ").append(pack.getPrice()).append("\n");
                }
            }
        }

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(text.toString());
        message.setReplyMarkup(KeyboardBuilder.buildCartMenu(lang, user.getCart().isEmpty()));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendCartMenu(chatId, user);
        }
    }

    private void sendCartMenu(Long chatId, User user) {
        String lang = user.getLanguage();
        StringBuilder text = new StringBuilder();

        if (user.getCart().isEmpty()) {
            text.append(Localization.get("cart_empty", lang));
        } else {
            text.append(Localization.get("cart_items", lang));
            int index = 1;
            for (String item : user.getCart().getItems()) {
                StickerPack pack = StickerPack.getById(item);
                if (pack != null) {
                    text.append(index++).append(". ").append(pack.getName(lang))
                            .append(" - ").append(pack.getPrice()).append("\n");
                }
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text.toString());
        message.setReplyMarkup(KeyboardBuilder.buildCartMenu(lang, user.getCart().isEmpty()));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCheckout(Long chatId, Integer messageId, User user) {
        String lang = user.getLanguage();

        if (user.getCart().isEmpty()) {
            return;
        }

        // Calculate total price in Stars
        int totalStars = 0;
        List<LabeledPrice> prices = new ArrayList<>();
        StringBuilder description = new StringBuilder();

        for (String packId : user.getCart().getItems()) {
            StickerPack pack = StickerPack.getById(packId);
            if (pack != null) {
                totalStars += pack.getPriceStars();
                description.append(pack.getName(lang)).append(", ");
            }
        }

        // Remove last comma
        if (description.length() > 0) {
            description.setLength(description.length() - 2);
        }

        // Add single price in Stars
        prices.add(new LabeledPrice("Total", totalStars));

        // Create invoice
        SendInvoice invoice = new SendInvoice();
        invoice.setChatId(chatId.toString());
        invoice.setTitle("PixiCats Premium Stickers");
        invoice.setDescription(description.toString());
        invoice.setPayload("pixicats_cart_" + chatId); // Unique payload
        invoice.setCurrency("XTR"); // Telegram Stars currency
        invoice.setPrices(prices);

        try {
            execute(invoice);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handlePreCheckoutQuery(org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery preCheckoutQuery) {
        // Always approve pre-checkout
        org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery answer =
                new org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery();
        answer.setPreCheckoutQueryId(preCheckoutQuery.getId());
        answer.setOk(true);

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleSuccessfulPayment(Message message) {
        Long chatId = message.getChatId();
        SuccessfulPayment payment = message.getSuccessfulPayment();
        User user = getOrCreateUser(chatId);
        String lang = user.getLanguage();

        // Build sticker pack links
        StringBuilder links = new StringBuilder();
        for (String packId : user.getCart().getItems()) {
            StickerPack pack = StickerPack.getById(packId);
            if (pack != null) {
                links.append(pack.getName(lang)).append("\n");
                links.append("→ ").append(pack.getStickerPackUrl()).append("\n\n");
            }
        }

        // Send confirmation with sticker pack links
        SendMessage confirmMessage = new SendMessage();
        confirmMessage.setChatId(chatId.toString());
        confirmMessage.setText(String.format(Localization.get("payment_success_msg", lang), links.toString()));
        confirmMessage.setReplyMarkup(KeyboardBuilder.buildMainMenu(lang));

        try {
            execute(confirmMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // Clear cart after successful payment
        user.getCart().clear();

        // Notify admin about purchase
        notifyAdmin(chatId, "SUCCESSFUL PAYMENT",
                "User purchased: " + payment.getTotalAmount() + " Stars\n" +
                        "Invoice payload: " + payment.getInvoicePayload());
    }

    private void handleClearCart(Long chatId, Integer messageId, User user) {
        user.getCart().clear();
        String lang = user.getLanguage();

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("cart_cleared", lang));
        message.setReplyMarkup(KeyboardBuilder.buildCartMenu(lang, true));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendClearCart(chatId, lang);
        }
    }

    private void sendClearCart(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("cart_cleared", lang));
        message.setReplyMarkup(KeyboardBuilder.buildCartMenu(lang, true));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // PAYMENT
    private void handlePayment(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("payment_success", lang));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(lang));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendPaymentSuccess(chatId, lang);
        }
    }

    private void sendPaymentSuccess(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("payment_success", lang));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(lang));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // SETTINGS
    private void editSettingsMenu(Long chatId, Integer messageId, String lang) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("btn_settings", lang));
        message.setReplyMarkup(KeyboardBuilder.buildSettingsMenu(lang));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendSettingsMenu(chatId, lang);
        }
    }

    private void sendSettingsMenu(Long chatId, String lang) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(Localization.get("btn_settings", lang));
        message.setReplyMarkup(KeyboardBuilder.buildSettingsMenu(lang));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editLanguageMenu(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText("Please select your language:\nПожалуйста, выберите язык:\nPalun vali oma keel:");
        message.setReplyMarkup(KeyboardBuilder.buildLanguageMenu());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendLanguageMenu(chatId);
        }
    }

    private void sendLanguageMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Please select your language:\nПожалуйста, выберите язык:\nPalun vali oma keel:");
        message.setReplyMarkup(KeyboardBuilder.buildLanguageMenu());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleLanguageChange(Long chatId, Integer messageId, String newLang, User user) {
        user.setLanguage(newLang);

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(Localization.get("language_changed", newLang));
        message.setReplyMarkup(KeyboardBuilder.buildMainMenu(newLang));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendMainMenu(chatId, newLang);
        }
    }

    // UTILITY
    private void answerCallback(String callbackId) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            // Ignore
        }
    }

    private User getOrCreateUser(Long chatId) {
        return users.computeIfAbsent(chatId, User::new);
    }
}
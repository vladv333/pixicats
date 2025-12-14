package utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardBuilder {

    public static InlineKeyboardMarkup buildMainMenu(String language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(createRow(button(loc("btn_free", language), "free")));
        keyboard.add(createRow(button(loc("btn_extended", language), "extended")));
        keyboard.add(createRow(button(loc("btn_exclusive", language), "exclusive")));
        keyboard.add(createRow(button(loc("btn_store", language), "store")));

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button(loc("btn_support", language), "support"));
        row.add(button(loc("btn_settings", language), "settings"));
        keyboard.add(row);

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildLanguageMenu() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(createRow(button("🇬🇧 English", "lang_en")));
        keyboard.add(createRow(button("🇷🇺 Русский", "lang_ru")));
        keyboard.add(createRow(button("🇪🇪 Eesti", "lang_et")));

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildShopMenu(String language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        var packs = model.StickerPack.getAllPacks();
        for (var entry : packs.entrySet()) {
            var pack = entry.getValue();
            keyboard.add(createRow(button(pack.getName(language), entry.getKey())));
        }

        keyboard.add(createRow(button(loc("btn_cart", language), "cart")));
        keyboard.add(createRow(button(loc("btn_back", language), "menu")));

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildPackMenu(String packId, String language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(createRow(button(loc("btn_add_cart", language), "add_" + packId)));
        keyboard.add(createRow(button(loc("btn_back", language), "store")));

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildCartMenu(String language, boolean isEmpty) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (!isEmpty) {
            keyboard.add(createRow(button(loc("btn_checkout", language), "checkout")));
            keyboard.add(createRow(button(loc("btn_clear_cart", language), "clear_cart")));
        }

        keyboard.add(createRow(button(loc("btn_back", language), "store")));

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildBackButton(String language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(createRow(button(loc("btn_back", language), "menu")));
        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildSettingsMenu(String language) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(createRow(button(loc("btn_language", language), "language")));
        keyboard.add(createRow(button(loc("btn_back", language), "menu")));

        markup.setKeyboard(keyboard);
        return markup;
    }

    public static InlineKeyboardMarkup buildPaymentMenu(String language, String priceButton) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(createRow(button(loc(priceButton, language), "pay")));
        keyboard.add(createRow(button(loc("btn_back", language), "menu")));

        markup.setKeyboard(keyboard);
        return markup;
    }

    private static InlineKeyboardButton button(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private static List<InlineKeyboardButton> createRow(InlineKeyboardButton... buttons) {
        return List.of(buttons);
    }

    private static String loc(String key, String language) {
        return localization.Localization.get(key, language);
    }
}
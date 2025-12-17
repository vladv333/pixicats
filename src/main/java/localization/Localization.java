package localization;

import java.util.HashMap;
import java.util.Map;

public class Localization {
    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        // English translations
        Map<String, String> en = new HashMap<>();
        en.put("welcome", "🐱 Welcome to PixiCats!\n\nYour cozy place for adorable stickers!\n\nChoose your option:");
        en.put("menu", "Main Menu");

        // Main menu buttons
        en.put("btn_free", "🐾 Free");
        en.put("btn_extended", "🚀 Extended - 5€");
        en.put("btn_exclusive", "🎨 Exclusive - 15€");
        en.put("btn_store", "🏪 Store - 10€");
        en.put("btn_support", "💬 Support");
        en.put("btn_settings", "⚙️ Settings");
        en.put("btn_language", "🌍 Change Language");
        en.put("btn_back", "⬅️ Back");

        // Free pack
        en.put("free_title", "🐾 Free Pack\n\nUpload your photos and get 1 to 5 custom stickers. Perfect for trying it out!\n\n📸 Send me 1-5 photos of your choice:");
        en.put("free_received", "✅ Photos received! We'll create your stickers soon.\n\nYou'll be notified when ready!");

        // Extended pack
        en.put("extended_title", "🚀 Extended Pack - 5€\n\nA full pack of 10 high-quality stickers featuring your images.\n\n📸 Send me up to 10 photos:");
        en.put("extended_received", "✅ Photos received!\n\nTotal: 5€\n\nClick 'Pay' to continue:");
        en.put("btn_pay", "💳 Pay 5€");

        // Exclusive pack
        en.put("exclusive_title", "🎨 Exclusive Pack - 15€\n\nWant something special? Our designer will create a fully custom sticker pack from scratch according to your wishes.\n\nPlease describe:\n1️⃣ Number of stickers\n2️⃣ Theme and style\n3️⃣ Special requests\n\nSend your request as a message:");
        en.put("exclusive_received", "✅ Your request has been sent to our designer!\n\nTotal: 15€\n\nClick 'Pay' to continue:");
        en.put("btn_pay_exclusive", "💳 Pay 15€");

        // Store
        en.put("store_title", "🏪 Premium Store - 10€\n\nReady-made premium packs! A collection of thematic PixiCats stickers for every taste.\n\nBuy and use them instantly!");
        en.put("pack_details", "10 premium stickers\nPrice: 10€");
        en.put("btn_add_cart", "➕ Add to Cart");
        en.put("added_to_cart", "✅ Added to cart!");
        en.put("btn_cart", "🛒 Cart");
        en.put("cart_empty", "Your cart is empty");
        en.put("cart_items", "🛒 Your Cart:\n\n");
        en.put("btn_checkout", "💳 Checkout");
        en.put("btn_clear_cart", "🗑️ Clear Cart");
        en.put("checkout_success", "🎉 Thank you for your order!\n\nYour sticker packs:\n\n%s\n\n💡 Click the links above to add stickers to Telegram!");
        en.put("cart_cleared", "🗑️ Cart cleared");

        // Support
        en.put("support_title", "💬 Support\n\nNeed help? Have questions?\n\nSend your message and our administrator will reply as soon as possible:");
        en.put("support_received", "✅ Your message has been sent to support!\n\nWe'll reply soon.");

        // Settings
        en.put("language_select", "Please select your language:");
        en.put("language_changed", "✅ Language changed to English");

        // Payment stubs
        en.put("payment_success", "✅ Payment successful!\n\nWe're processing your order. You'll be notified when ready!");

        translations.put("en", en);

        // Russian translations
        Map<String, String> ru = new HashMap<>();
        ru.put("welcome", "🐱 Добро пожаловать в PixiCats!\n\nВаше уютное место для милых стикеров!\n\nВыберите вариант:");
        ru.put("menu", "Главное меню");

        // Main menu buttons
        ru.put("btn_free", "🐾 Бесплатно");
        ru.put("btn_extended", "🚀 Расширенный - 5€");
        ru.put("btn_exclusive", "🎨 Эксклюзив - 15€");
        ru.put("btn_store", "🏪 Магазин - 10€");
        ru.put("btn_support", "💬 Поддержка");
        ru.put("btn_settings", "⚙️ Настройки");
        ru.put("btn_language", "🌍 Изменить язык");
        ru.put("btn_back", "⬅️ Назад");

        // Free pack
        ru.put("free_title", "🐾 Бесплатный набор\n\nЗагрузите свои фото и получите от 1 до 5 стикеров. Идеально для пробы!\n\n📸 Отправьте мне 1-5 фотографий на выбор:");
        ru.put("free_received", "✅ Фото получены! Скоро создадим ваши стикеры.\n\nМы уведомим вас, когда они будут готовы!");

        // Extended pack
        ru.put("extended_title", "🚀 Расширенный набор - 5€\n\nПолный набор из 10 качественных стикеров с вашими изображениями.\n\n📸 Отправьте мне до 10 фотографий:");
        ru.put("extended_received", "✅ Фото получены!\n\nИтого: 5€\n\nНажмите 'Оплатить' для продолжения:");
        ru.put("btn_pay", "💳 Оплатить 5€");

        // Exclusive pack
        ru.put("exclusive_title", "🎨 Эксклюзивный набор - 15€\n\nХотите что-то особенное? Наш дизайнер создаст полностью уникальный набор стикеров с нуля по вашим пожеланиям.\n\nОпишите:\n1️⃣ Количество стикеров\n2️⃣ Тему и стиль\n3️⃣ Особые пожелания\n\nОтправьте ваш запрос сообщением:");
        ru.put("exclusive_received", "✅ Ваш запрос отправлен дизайнеру!\n\nИтого: 15€\n\nНажмите 'Оплатить' для продолжения:");
        ru.put("btn_pay_exclusive", "💳 Оплатить 15€");

        // Store
        ru.put("store_title", "🏪 Премиум магазин - 10€\n\nГотовые премиум наборы! Коллекция тематических стикеров PixiCats на любой вкус.\n\nКупите и используйте мгновенно!");
        ru.put("pack_details", "10 премиум стикеров\nЦена: 10€");
        ru.put("btn_add_cart", "➕ Добавить в корзину");
        ru.put("added_to_cart", "✅ Добавлено в корзину!");
        ru.put("btn_cart", "🛒 Корзина");
        ru.put("cart_empty", "Ваша корзина пуста");
        ru.put("cart_items", "🛒 Ваша корзина:\n\n");
        ru.put("btn_checkout", "💳 Оформить заказ");
        ru.put("btn_clear_cart", "🗑️ Очистить корзину");
        ru.put("checkout_success", "🎉 Спасибо за заказ!\n\nВаши стикерпаки:\n\n%s\n\n💡 Нажмите на ссылки выше, чтобы добавить стикеры в Telegram!");
        ru.put("cart_cleared", "🗑️ Корзина очищена");

        // Support
        ru.put("support_title", "💬 Поддержка\n\nНужна помощь? Есть вопросы?\n\nОтправьте ваше сообщение, и наш администратор ответит как можно скорее:");
        ru.put("support_received", "✅ Ваше сообщение отправлено в поддержку!\n\nМы скоро ответим.");

        // Settings
        ru.put("language_select", "Пожалуйста, выберите язык:");
        ru.put("language_changed", "✅ Язык изменён на русский");

        // Payment stubs
        ru.put("payment_success", "✅ Оплата прошла успешно!\n\nМы обрабатываем ваш заказ. Уведомим, когда всё будет готово!");

        translations.put("ru", ru);

        // Estonian translations
        Map<String, String> et = new HashMap<>();
        et.put("welcome", "🐱 Tere tulemast PixiCats!\n\nSinu hubane koht armsate stickeride jaoks!\n\nVali oma variant:");
        et.put("menu", "Peamenüü");

        // Main menu buttons
        et.put("btn_free", "🐾 Tasuta");
        et.put("btn_extended", "🚀 Laiendatud - 5€");
        et.put("btn_exclusive", "🎨 Eksklusiivselt - 15€");
        et.put("btn_store", "🏪 Pood - 10€");
        et.put("btn_support", "💬 Abi");
        et.put("btn_settings", "⚙️ Seaded");
        et.put("btn_language", "🌍 Muuda keelt");
        et.put("btn_back", "⬅️ Tagasi");

        // Free pack
        et.put("free_title", "🐾 Tasuta pakett\n\nLaadige üles oma fotod ja saage 1 kuni 5 kohandatud kleepsu. Ideaalne proovimiseks!\n\n📸 Saatke mulle 1-5 fotot oma valikul:");
        et.put("free_received", "✅ Fotod saadud! Loome teie kleepsud peagi.\n\nTeavitame teid, kui need on valmis!");

        // Extended pack
        et.put("extended_title", "🚀 Laiendatud pakett - 5€\n\nTäielik pakett 10 kvaliteetsest kleepsust teie piltidega.\n\n📸 Saatke mulle kuni 10 fotot:");
        et.put("extended_received", "✅ Fotod saadud!\n\nKokku: 5€\n\nKlõpsake 'Maksa' jätkamiseks:");
        et.put("btn_pay", "💳 Maksa 5€");

        // Exclusive pack
        et.put("exclusive_title", "🎨 Eksklusiivselt pakett - 15€\n\nKas soovite midagi erilist? Meie disainer loob täiesti kohandatud kleepsupaketi nullist vastavalt teie soovidele.\n\nKirjeldage:\n1️⃣ Kleepsude arv\n2️⃣ Teema ja stiil\n3️⃣ Erinõuded\n\nSaatke oma taotlus sõnumina:");
        et.put("exclusive_received", "✅ Teie taotlus on saadetud disainerile!\n\nKokku: 15€\n\nKlõpsake 'Maksa' jätkamiseks:");
        et.put("btn_pay_exclusive", "💳 Maksa 15€");

        // Store
        et.put("store_title", "🏪 Premium pood - 10€\n\nValmis premium paketid! Temaatiliste PixiCats kleepsude kollektsioon igale maitsele.\n\nOstke ja kasutage kohe!");
        et.put("pack_details", "10 premium kleepsu\nHind: 10€");
        et.put("btn_add_cart", "➕ Lisa ostukorvi");
        et.put("added_to_cart", "✅ Lisatud ostukorvi!");
        et.put("btn_cart", "🛒 Ostukorv");
        et.put("cart_empty", "Teie ostukorv on tühi");
        et.put("cart_items", "🛒 Teie ostukorv:\n\n");
        et.put("btn_checkout", "💳 Maksmine");
        et.put("btn_clear_cart", "🗑️ Tühjenda ostukorv");
        et.put("checkout_success", "🎉 Täname tellimuse eest!\n\nTeie kleepsupaketid:\n\n%s\n\n💡 Klõpsake ülalolevatest linkidest, et lisada kleepsud Telegrami!");
        et.put("cart_cleared", "🗑️ Ostukorv tühjendatud");

        // Support
        et.put("support_title", "💬 Abi\n\nVajate abi? On küsimusi?\n\nSaatke oma sõnum ja meie administraator vastab niipea kui võimalik:");
        et.put("support_received", "✅ Teie sõnum on saadetud toele!\n\nVastame peagi.");

        // Settings
        et.put("language_select", "Palun vali oma keel:");
        et.put("language_changed", "✅ Keel muudetud eesti keeleks");

        // Payment stubs
        et.put("payment_success", "✅ Makse õnnestus!\n\nTöötleme teie tellimust. Teavitame teid, kui see on valmis!");

        translations.put("et", et);
    }

    public static String get(String key, String language) {
        Map<String, String> langMap = translations.getOrDefault(language, translations.get("en"));
        return langMap.getOrDefault(key, key);
    }
}
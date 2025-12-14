package model;

public class User {
    private final Long chatId;
    private String language;
    private Cart cart;

    public User(Long chatId) {
        this.chatId = chatId;
        this.language = "en"; // default language
        this.cart = new Cart();
    }

    public Long getChatId() {
        return chatId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
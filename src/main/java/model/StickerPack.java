package model;

import java.util.HashMap;
import java.util.Map;

public class StickerPack {
    private final String id;
    private final String nameEn;
    private final String nameRu;
    private final String nameEt;
    private final String price;
    private final int priceStars;
    private final String previewUrl;
    private final String stickerPackUrl;

    public StickerPack(String id, String nameEn, String nameRu, String nameEt,
                       String price, int priceStars, String previewUrl, String stickerPackUrl) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameRu = nameRu;
        this.nameEt = nameEt;
        this.price = price;
        this.priceStars = priceStars;
        this.previewUrl = previewUrl;
        this.stickerPackUrl = stickerPackUrl;
    }

    public String getId() {
        return id;
    }

    public String getName(String language) {
        return switch (language) {
            case "ru" -> nameRu;
            case "et" -> nameEt;
            default -> nameEn;
        };
    }

    public String getPrice() {
        return price;
    }

    public int getPriceStars() {
        return priceStars;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getStickerPackUrl() {
        return stickerPackUrl;
    }

    private static final Map<String, StickerPack> CATALOG = new HashMap<>();

    static {
        CATALOG.put("pack_1", new StickerPack(
                "pack_1",
                "Bocchi",
                "Bocchi",
                "Bocchi",
                "1 ⭐",
                1,
                "https://i.pinimg.com/originals/65/39/5e/65395e7d4d8ea15941c538b54e293f60.gif",
                "https://t.me/addstickers/pa_mVWgi4mzzGm39ly7Cp4W_by_SigStick12Bot"
        ));

        CATALOG.put("pack_2", new StickerPack(
                "pack_2",
                "😹Pixicats",
                "😹 Pixicats",
                "😹 Pixicats",
                "1 ⭐",
                1,
                "https://i.ibb.co/V070qxQS/IMG-0420.png",
                "https://t.me/addstickers/pixicats1"
        ));
    }

    public static StickerPack getById(String id) {
        return CATALOG.get(id);
    }

    public static Map<String, StickerPack> getAllPacks() {
        return new HashMap<>(CATALOG);
    }
}
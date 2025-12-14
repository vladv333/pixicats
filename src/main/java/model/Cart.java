package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<String> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public List<String> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.size();
    }
}
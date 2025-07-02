package io.dcloud.dcloud_a;

import java.util.HashMap;
import java.util.Map;

/* compiled from: EncryptionSingleton.java */
/* loaded from: classes.dex  old b*/
public class EncryptionSingleton {

    private final HashMap<String, Map<String, String>> storage;

    // Приватний конструктор
    private EncryptionSingleton() {
        this.storage = new HashMap<>();
    }

    // Статичний клас-холдер (патерн Singleton)
    private static class Holder {
        private static final EncryptionSingleton INSTANCE = new EncryptionSingleton();
    }

    // Метод для отримання екземпляра
    public static EncryptionSingleton getInstance() {
        return Holder.INSTANCE;
    }

    // Зберегти дані
    public void put(String key, Map<String, String> value) {
        storage.put(key, value);
    }

    // Отримати дані
    public Map<String, String> get(String key) {
        return storage.get(key);
    }
}

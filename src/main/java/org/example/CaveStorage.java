package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * Пещера Хранителя — древнее хранилище провианта.
 */
public class CaveStorage {
    private String caveName;
    private String lore;
    private int capacity;
    private Map<String, Provision> provisions;

    public CaveStorage(String caveName, String lore, int capacity) {
        this.caveName = caveName;
        this.lore = lore;
        this.capacity = capacity;
        this.provisions = new HashMap<>();
    }

    public String getCaveName() {
        return caveName;
    }

    public String getLore() {
        return lore;
    }

    /**
     * Получить текущее количество предметов в хранилище.
     */
    public int getCurrentLoad() {
        int total = 0;
        for (Provision p : provisions.values()) {
            total += p.getQuantity();
        }
        return total;
    }

    /**
     * Проверить, есть ли свободное место.
     */
    public boolean hasSpace(int amount) {
        return getCurrentLoad() + amount <= capacity;
    }

    /**
     * Добавить провиант в хранилище.
     * @throws IllegalStateException если в хранилище недостаточно места
     */
    public void addProvision(Provision provision) {
        int amount = provision.getQuantity();
        if (!hasSpace(amount)) {
            int spaceLeft = capacity - getCurrentLoad();
            throw new IllegalStateException(
                "Недостаточно места в хранилище! Свободно: " + spaceLeft + " ед., попытка добавить: " + amount + " ед."
            );
        }

        String key = provision.getName().toLowerCase();
        if (provisions.containsKey(key)) {
            Provision existing = provisions.get(key);
            existing.setQuantity(existing.getQuantity() + provision.getQuantity());
            System.out.println("➕ Добавлено ещё " + provision.getQuantity() + " ед. \"" + provision.getName() + "\". Всего: " + existing.getQuantity());
        } else {
            provisions.put(key, provision);
            System.out.println("➕ В хранилище добавлено: " + provision.getName() + " (" + provision.getQuantity() + " ед.)");
        }
    }

    /**
     * Получить провиант из хранилища.
     * @throws ProvisionNotFoundException если провиант не найден
     * @throws InsufficientProvisionException если недостаточно провианта
     */
    public Provision getProvision(String name, int amount) {
        String key = name.toLowerCase();
        if (!provisions.containsKey(key)) {
            throw new ProvisionNotFoundException(name);
        }

        Provision provision = provisions.get(key);
        if (provision.getQuantity() < amount) {
            throw new InsufficientProvisionException(name, provision.getQuantity(), amount);
        }

        provision.setQuantity(provision.getQuantity() - amount);
        System.out.println("✅ Получено " + amount + " ед. \"" + name + "\"");

        if (provision.getQuantity() == 0) {
            provisions.remove(key);
            System.out.println("🗑️ \"" + name + "\" полностью израсходован!");
        }

        return provision;
    }

    /**
     * Показать все запасы в пещере.
     */
    public void showInventory() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("  🏔️ " + caveName + " ═══════════════════════════════════════════════════════════");
        System.out.println("  " + lore);
        System.out.println("═══════════════════════════════════════════════════════════");

        if (provisions.isEmpty()) {
            System.out.println("  📭 Хранилище пусто...");
        } else {
            System.out.println("  📦 ЗАПАСЫ ПРОВИАНТА:");
            int totalNutrition = 0;
            for (Provision p : provisions.values()) {
                System.out.println("\n  " + p.getLoreDescription());
                totalNutrition += p.getQuantity() * p.getNutritionValue();
            }
            System.out.println("\n  ───────────────────────────────────────────────────────");
            System.out.println("  💪 ОБЩАЯ ПИТАТЕЛЬНАЯ ЦЕННОСТЬ: " + totalNutrition);
        }
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }

    /**
     * Проверить, есть ли провиант с указанным именем.
     */
    public boolean hasProvision(String name) {
        return provisions.containsKey(name.toLowerCase());
    }

    /**
     * Получить количество провианта по имени.
     */
    public int getQuantity(String name) {
        Provision p = provisions.get(name.toLowerCase());
        return p != null ? p.getQuantity() : 0;
    }
}

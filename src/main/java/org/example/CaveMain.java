package org.example;

/**
 * Главный класс — точка входа в историю о Пещере Хранителя.
 * Демонстрирует систему хранения и управления провиантом с обработкой ошибок.
 */
public class CaveMain {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║         📜 ЛЕГЕНДА О ПЕЩЕРЕ ХРАНИТЕЛЯ 📜                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        CaveStorage cave = new CaveStorage(
            "Пещера Забытых Запасов",
            "Древнее хранилище, вырезанное в скалах первыми поселенцами.",
            150
        );

        System.out.println("🏔️ Вы обнаружили: " + cave.getCaveName());
        System.out.println("📊 Вместимость: 150 ед.");
        System.out.println();

        cave.showInventory();

        System.out.println("📦 НАПОЛНЕНИЕ ХРАНИЛИЩА...");
        System.out.println();

        cave.addProvision(new Provision("Сушёное мясо оленя", "Мясо, вяленное на северном ветру", 50, 25));
        cave.addProvision(new Provision("Хлеб путника", "Чёрный хлеб с тмином", 100, 10));

        System.out.println("🌟 ХРАНИЛИЩЕ ГОТОВО К НОВЫМ ИСПЫТАНИЯМ! 🌟");
    }
}

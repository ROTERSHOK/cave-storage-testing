package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class CaveStorageTest {

    private CaveStorage cave;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static final String CAVE_NAME = "Пещера Забытых Запасов";
    private static final String LORE = "Древнее хранилище, вырезанное в скалах.";
    private static final int CAPACITY = 150;

    @BeforeEach
    void setUp() {
        cave = new CaveStorage(CAVE_NAME, LORE, CAPACITY);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    @DisplayName("Should initialize with given name, lore, and empty load")
    void constructorInitialize() {
        Assertions.assertAll(
            () -> Assertions.assertEquals(CAVE_NAME, cave.getCaveName()),
            () -> Assertions.assertEquals(LORE, cave.getLore()),
            () -> Assertions.assertEquals(0, cave.getCurrentLoad())
        );
    }

    @Test
    @DisplayName("Should return true for 1 unit in empty storage")
    void hasSpaceForOne() {
        Assertions.assertTrue(cave.hasSpace(1));
    }

    @Test
    @DisplayName("Should return true when storage is almost full")
    void hasSpaceCheck() {
        Assertions.assertTrue(cave.hasSpace(149));
    }

    @Test
    @DisplayName("Should return true exactly at capacity boundary")
    void hasSpaceExact() {
        Assertions.assertTrue(cave.hasSpace(150));
    }

    @Test
    @DisplayName("Should return false when capacity is exceeded")
    void hasSpaceOverflow() {
        Assertions.assertFalse(cave.hasSpace(151));
    }

    @Test
    @DisplayName("Should consider current load when checking space")
    void spaceWithLoad() {
        cave.addProvision(new Provision("Хлеб", "Описание", 100, 50));
        Assertions.assertTrue(cave.hasSpace(50));
        Assertions.assertFalse(cave.hasSpace(51));
    }

    @Test
    @DisplayName("Should add provision to empty storage")
    void addProvisionTest() {
        Provision bread = new Provision("Хлеб", "Свежий хлеб", 20, 50);
        cave.addProvision(bread);

        Assertions.assertTrue(cave.hasProvision("Хлеб"));
        Assertions.assertEquals(20, cave.getQuantity("Хлеб"));
        Assertions.assertEquals(20, cave.getCurrentLoad());
    }

    @Test
    @DisplayName("Should merge provisions with the same name")
    void addSameProvision() {
        cave.addProvision(new Provision("Хлеб", "Описание", 10, 50));
        cave.addProvision(new Provision("Хлеб", "Другое описание", 5, 50));

        Assertions.assertEquals(15, cave.getQuantity("Хлеб"));
    }

    @Test
    @DisplayName("Should throw when adding beyond total capacity")
    void addTooMuch() {
        Provision bigProvision = new Provision("Глыба", "Очень большая", 200, 10);

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class,
                () -> cave.addProvision(bigProvision));

        Assertions.assertTrue(ex.getMessage().contains("Недостаточно места"));
    }

    @Test
    @DisplayName("Should throw when adding beyond remaining space")
    void addMoreThanAvailable() {
        cave.addProvision(new Provision("Хлеб", "Описание", 100, 50));
        Provision overflow = new Provision("Сыр", "Описание", 60, 40);

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class,
                () -> cave.addProvision(overflow));

        Assertions.assertTrue(ex.getMessage().contains("50"));
        Assertions.assertTrue(ex.getMessage().contains("60"));
    }

    @Test
    @DisplayName("Should fill exactly to capacity limit")
    void addExactly() {
        cave.addProvision(new Provision("Хлеб", "Описание", 100, 50));
        Assertions.assertDoesNotThrow(() -> cave.addProvision(new Provision("Сыр", "Описание", 50, 40)));
        Assertions.assertEquals(150, cave.getCurrentLoad());
    }

    @Test
    @DisplayName("Should return true for existing provision")
    void hasProvisionCheck() {
        cave.addProvision(new Provision("Хлеб", "Описание", 10, 50));
        Assertions.assertTrue(cave.hasProvision("Хлеб"));
    }

    @Test
    @DisplayName("Should return false for missing provision")
    void noProvision() {
        Assertions.assertFalse(cave.hasProvision("Хлеб"));
    }

    @Test
    @DisplayName("Should ignore case when checking provisions")
    void provisionCaseTest() {
        cave.addProvision(new Provision("Хлеб", "Описание", 10, 50));
        Assertions.assertTrue(cave.hasProvision("хлеб"));
        Assertions.assertTrue(cave.hasProvision("ХЛЕБ"));
    }

    @Test
    @DisplayName("Should return correct quantity for existing provision")
    void getQuantityTest() {
        cave.addProvision(new Provision("Хлеб", "Описание", 25, 50));
        Assertions.assertEquals(25, cave.getQuantity("Хлеб"));
    }

    @Test
    @DisplayName("Should return 0 for non-existing provision")
    void getQuantityNull() {
        Assertions.assertEquals(0, cave.getQuantity("Хлеб"));
    }

    @Test
    @DisplayName("Should decrease quantity when provision is taken")
    void getProvisionTest() {
        cave.addProvision(new Provision("Хлеб", "Описание", 20, 50));
        Provision result = cave.getProvision("Хлеб", 5);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(15, cave.getQuantity("Хлеб"));
    }

    @Test
    @DisplayName("Should remove provision when fully consumed")
    void getAllProvision() {
        cave.addProvision(new Provision("Хлеб", "Описание", 10, 50));
        cave.getProvision("Хлеб", 10);

        Assertions.assertFalse(cave.hasProvision("Хлеб"));
        Assertions.assertEquals(0, cave.getQuantity("Хлеб"));
    }

    @Test
    @DisplayName("Missing provision -> ProvisionNotFoundException")
    void provisionNotFound() {
        ProvisionNotFoundException ex = Assertions.assertThrows(ProvisionNotFoundException.class,
                () -> cave.getProvision("Несуществующий", 5));

        Assertions.assertTrue(ex.getMessage().contains("не найден"));
    }

    @Test
    @DisplayName("Request more than available -> InsufficientProvisionException")
    void notEnoughProvision() {
        cave.addProvision(new Provision("Хлеб", "Описание", 5, 50));

        InsufficientProvisionException ex = Assertions.assertThrows(InsufficientProvisionException.class,
                () -> cave.getProvision("Хлеб", 10));

        Assertions.assertTrue(ex.getMessage().contains("5"));
        Assertions.assertTrue(ex.getMessage().contains("10"));
    }

    @Test
    @DisplayName("Should ignore case when retrieving provisions")
    void getProvisionCase() {
        cave.addProvision(new Provision("Хлеб", "Описание", 10, 50));
        cave.getProvision("хлеб", 3);

        Assertions.assertEquals(7, cave.getQuantity("Хлеб"));
    }

    @Test
    @DisplayName("Should display empty storage message")
    void showEmpty() {
        cave.showInventory();
        String output = outContent.toString();
        Assertions.assertAll(
            () -> Assertions.assertTrue(output.contains(CAVE_NAME)),
            () -> Assertions.assertTrue(output.contains("📭 Хранилище пусто...") || output.contains("empty"))
        );
    }

    @Test
    @DisplayName("Should display provisions with correct totals")
    void showFilled() {
        cave.addProvision(new Provision("Хлеб", "Описание", 10, 50));
        cave.showInventory();
        String output = outContent.toString();

        Assertions.assertAll(
            () -> Assertions.assertTrue(output.contains(CAVE_NAME)),
            () -> Assertions.assertTrue(output.contains("Хлеб")),
            () -> Assertions.assertTrue(output.contains("10")),
            () -> Assertions.assertTrue(output.contains("50")),
            () -> Assertions.assertTrue(output.contains("500"))
        );
    }
}
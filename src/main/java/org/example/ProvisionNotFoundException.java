package org.example;

/**
 * Исключение, выбрасываемое когда провиант не найден в хранилище.
 */
public class ProvisionNotFoundException extends RuntimeException {
    public ProvisionNotFoundException(String name) {
        super("Провиант \"" + name + "\" не найден в хранилище!");
    }
}

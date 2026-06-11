package org.example;

/**
 * Исключение, выбрасываемое когда недостаточно провианта в хранилище.
 */
public class InsufficientProvisionException extends RuntimeException {
    public InsufficientProvisionException(String name, int available, int requested) {
        super("Недостаточно \"" + name + "\"! Доступно: " + available + " ед., запрошено: " + requested + " ед.");
    }
}

package Module;


import java.util.NoSuchElementException;

/**
 * Входная точка в программу
 * @autor Svytoq
 * @version 1.0
 */
public class ClientSide {
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.out.println("Отключение клиента");
                }
            });
            System.out.println("Запуск клиента...");
            ClientConnection client = new ClientConnection();
            client.connection();
        } catch (NoSuchElementException | ClassNotFoundException e) {
            // для ctrl+D
        } catch (NullPointerException e) {
            System.out.println("Ошибка соединения");
        }}
}

package Module;

import java.io.IOException;

/**
 * Входная точка в программу
 * @autor Svytoq
 * @version 1.0
 */
public class ServerSide {

    public ServerSide() {
    }

    public static void main(String[] args) {
        ServerConnection serverConnection = new ServerConnection();

        try {
            if (args != null && args.length != 0) {
                serverConnection.load(args[0]);
            } else {
                System.out.println("Не был передан путь к файлу при запуске программы");
            }

            serverConnection.connection();
        } catch (IOException var3) {
            System.out.println("Поток ввода закрыт");
        }

    }
}

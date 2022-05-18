package Module;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Входная точка в программу
 * @autor Svytoq
 * @version 1.0
 */
public class ServerSide {

    public ServerSide() {
    }
    private static final Logger logger = Logger.getLogger("ServerLogger");

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        logger.log(Level.INFO,"запуск сервера");
        ServerConnection serverConnection = new ServerConnection();
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    logger.log(Level.INFO,"Отключение сервера");
                }
            });
            serverConnection.connection("properties");
        } catch (NoSuchElementException e) {
            //
        }
    }
}

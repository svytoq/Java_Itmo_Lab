package Module;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerReceiver implements Runnable {

    private SelectionKey key;
    private CollectionManager collectionManager;
    private BDActivity bdActivity;
    private ExecutorService poolSend;
    private ExecutorService poolHandler;
    Logger logger = Logger.getLogger("ServerLogger");

    public ServerReceiver(SelectionKey key, CollectionManager collectionManager, BDActivity bdActivity, ExecutorService poolSend, ExecutorService poolHandler) {
        this.key = key;
        this.collectionManager = collectionManager;
        this.bdActivity = bdActivity;
        this.poolSend = poolSend;
        this.poolHandler = poolHandler;
    }

    /**
     * Метод получает команду или логин с паролем от клиента
     */
    @Override
    public void run() {
        try {
            CommandDescription commandDescription = null;
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            SocketChannel channel = (SocketChannel) key.channel();
            int available = channel.read(buffer);
            if (available > -1) {
                while (available > 0) {
                    available = channel.read(buffer);
                }
                byte[] buf = buffer.array();
                ObjectInputStream fromClient = new ObjectInputStream(new ByteArrayInputStream(buf));
                commandDescription = (CommandDescription) fromClient.readObject();
                fromClient.close();
                buffer.clear();
                if (commandDescription.getCommand() == Command.REG || commandDescription.getCommand() == Command.SIGN) {
                    logger.log(Level.INFO,"От клиента получен логин и пароль");
                } else {
                    logger.log(Level.INFO,"От клиента получена команда " + commandDescription.getCommand().toString());
                }
                //serverHandler.handler(commandDescription, collectionManager, bdActivity, poolSend, key);
                poolHandler.submit(new ServerHandler(commandDescription,collectionManager,bdActivity, poolSend,key));
            }
            if (available == -1) {
                key.cancel();
            }
        } catch (IOException | ClassNotFoundException e) {
            // Все под контролем
        }
    }
}
package Module;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSender implements Runnable {
    private SelectionKey key;
    private String answer;

    public ServerSender(SelectionKey key, String answer) {
        this.key = key;
        this.answer = answer;
    }

    Logger logger = Logger.getLogger("ServerLogger");

    /**
     * Метод отправляет результат выполнения команды, регистрации или авторизации клиенту
     */
    public void run() {
        SocketChannel channel = (SocketChannel) key.channel();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream toClient = new ObjectOutputStream(baos)) {
            toClient.writeObject(answer);
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            int available = channel.write(buffer);
            while (available > 0) {
                available = channel.write(buffer);
            }
            buffer.clear();
            buffer.flip();
            logger.log(Level.INFO,"Результат отправлен клиенту");
        } catch (IOException e) {
            //
        }
    }
}
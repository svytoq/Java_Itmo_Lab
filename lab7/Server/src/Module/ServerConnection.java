package Module;

import Manager.CommandReader;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс отвечающий за соединение с клиентом
 * @autor Svytoq
 * @version 1.0
 */
public class ServerConnection {
    /**
     * Поле исполнителя команд связанных с файлом
     */

    /**
     * Поле исполнителя команд связанных с коллекцией
     */
    private CollectionManager collectionManager = new CollectionManager(new TreeSet());


    /**
     * Поле конкретной команды
     */
    private CommandDescription commandDescription;
    /** Сканер для чтения информации из командной строки*/
    private Scanner scanner;

    private BDActivity bdActivity = new BDActivity();
    Logger logger = Logger.getLogger("ServerLogger");
    private ExecutorService poolSend = Executors.newFixedThreadPool(10);
    private ExecutorService poolHandler = Executors.newCachedThreadPool();
    private ExecutorService poolReceiver = Executors.newFixedThreadPool(10);
    /**
     * Конструктор - создание нового объекта
     */
    public ServerConnection() {

        this.scanner = new Scanner(System.in);
    }

    /**
     * Метод для принятия подключений от пользователей
     */
    public void connection(String file) throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            try {
                logger.log(Level.INFO,"Введите порт");
                int port = Integer.parseInt(scanner.nextLine());
                Selector selector = Selector.open();
                try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
                    socketChannel.bind(new InetSocketAddress(port));
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    String infCol = collectionManager.loadToCol(file, bdActivity);
                    logger.log(Level.INFO,"Сервер ожидает подключения клиентов");
                    while (selector.isOpen()) {
                        int count = selector.select();
                        if (count == 0) {
                            continue;
                        }
                        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                        while (iter.hasNext()) {
                            SelectionKey key = iter.next();
                            try {
                                if (key.isAcceptable()) {
                                    SocketChannel channel = socketChannel.accept();
                                    logger.log(Level.INFO,"К серверу подключился клиент");
                                    channel.configureBlocking(false);
                                    channel.register(selector, SelectionKey.OP_WRITE);
                                }
                                if (key.isWritable()) {
                                    if (infCol == null) {
                                        poolSend.execute(new ServerSender(key, "Загружена коллекция c SQL" ));
                                        key.interestOps(SelectionKey.OP_READ);
                                    } else {
                                        poolSend.execute(new ServerSender(key, infCol));
                                        Thread.sleep(1000);
                                        System.exit(1);
                                    }
                                }
                                if (key.isReadable()) {
                                    poolReceiver.submit(new ServerReceiver(key, collectionManager, bdActivity, poolSend, poolHandler));
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                                iter.remove();
                            } catch (CancelledKeyException e) {
                                logger.log(Level.INFO,"Клиент отключился");
                                logger.log(Level.INFO,"Сервер ожидает подключения клиентов");
                                logger.info("Введите exit и нажмите ENTER - если хотите выключить сервер.");
                                Runnable save = () -> {
                                    try {
                                        serverMod();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                };
                                new Thread(save).start();
                            }
                        }
                    }
                }
            } catch (BindException e) {
                logger.log(Level.INFO,"Такой порт уже используется");
            } catch (NumberFormatException e) {
                logger.log(Level.INFO,"Порт не число или выходит за пределы");
            } catch (IllegalArgumentException e) {
                logger.log(Level.INFO,"Порт должен принимать значения от 1 до 65535");
            } catch (SocketException e) {
                logger.log(Level.INFO,"Недопустимый порт");
            }
        }
    }

    /**
     * Метод для обработки команд save и exit на сервере
     */
    public void serverMod() throws IOException {
        String s = "";
        CommandReader commandReader = new CommandReader();
        CommandDescription command = commandReader.readCommand();
        switch (command.getCommand()) {
            case EXIT:
                logger.info("Программа сервера успешно завершена.");
                System.exit(0);
                break;
            default:
                System.out.println("На сервере поддерживаются только команда exit.");

        }
    }
}
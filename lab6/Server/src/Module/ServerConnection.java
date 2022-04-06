package Module;

import Manager.CollectionManager;
import Manager.FileManager;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;

/**
 * Класс отвечающий соединение с клиентом
 * @autor Svytoq
 * @version 1.0
 */
public class ServerConnection {
    /**
     * Поле исполнителя команд связанных с файлом
     */
    private FileManager fileManager = new FileManager();
    /**
     * Поле исполнителя команд связанных с коллекцией
     */
    private CollectionManager collectionManager = new CollectionManager(new TreeSet());

    /**
     * Поле объекта для отправки и получения сообщения, выбора конкретной команды
     */
    private ServerWork server;
    /**
     * Поле конкретной команды
     */
    private CommandDescription commandDescription;
    /** Сканер для чтения информации из командной строки*/
    private Scanner scanner;
    /**
     * Конструктор - создание нового объекта
     */
    public ServerConnection() {
        this.server = new ServerWork(this.collectionManager, this.fileManager);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Метод для принятия подключений от пользователей
     */
    public void connection() throws IOException {
        while(true) {
            try {
                System.out.println("Введите порт");
                int port = Integer.parseInt(this.scanner.nextLine());
                Selector selector = Selector.open();
                ServerSocketChannel socketChannel = ServerSocketChannel.open();
                Throwable var4 = null;

                try {
                    socketChannel.bind(new InetSocketAddress(port));
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, 16);
                    System.out.println("Сервер запущен");
                    System.out.println("Сервер ожидает подключения");
                    server.getLogger().log(Level.INFO,"Сервер запущен");
                    server.getLogger().log(Level.INFO,"Сервер ожидает подключения");
                    while(selector.isOpen()) {
                        int count = selector.select();
                        if (count != 0) {
                            Iterator iter = selector.selectedKeys().iterator();

                            while(iter.hasNext()) {
                                SelectionKey key = (SelectionKey)iter.next();

                                try {
                                    if (key.isAcceptable()) {
                                        SocketChannel channel = socketChannel.accept();
                                        System.out.println("К серверу подключился клиент");
                                        server.getLogger().log(Level.INFO,"К серверу подключился клиент");
                                        channel.configureBlocking(false);
                                        channel.register(selector, 1);
                                    }

                                    if (key.isReadable()) {
                                        commandDescription = server.receiveCommand(key);
                                        server.getLogger().log(Level.INFO,"Получен запрос от клиента.");
                                    }

                                    if (key.isWritable()) {
                                        server.send(key, commandDescription);
                                        server.getLogger().info("Отправлен ответ клиенту.");
                                    }

                                    iter.remove();
                                } catch (Exception var21) {
                                    System.out.println("Один из клиентов отключился, введите save если хотите сохранить коллекцию и продолжить работу, exit если хотите сохранить коллекцию и отключить клиент, Enter если просто хотите продолжить работу");
                                    server.getLogger().log(Level.INFO,"Один из клиентов отключился.");
                                    server.serverMod();

                                    key.cancel();
                                }
                            }
                        }
                        }
                } catch (Throwable var22) {
                    var4 = var22;
                    throw var22;
                } finally {
                    if (socketChannel != null) {
                        if (var4 != null) {
                            try {
                                socketChannel.close();
                            } catch (Throwable var20) {
                                var4.addSuppressed(var20);
                            }
                        } else {
                            socketChannel.close();
                        }
                    }

                }
            } catch (BindException var24) {
                System.out.println("Такой порт уже используется");
                server.getLogger().log(Level.SEVERE,"Такой порт уже используется");
            } catch (NumberFormatException var25) {
                System.out.println("Порт не число или выходит за пределы");
                server.getLogger().log(Level.SEVERE,"Порт не число или выходит за пределы");
            } catch (IllegalArgumentException var26) {
                System.out.println("Порт должен принимать значения от 1 до 65535");
                server.getLogger().log(Level.SEVERE,"Порт должен принимать значения от 1 до 65535");
            } catch (SocketException var27) {
                System.out.println("Недопустимый порт");
                server.getLogger().log(Level.SEVERE,"Недопустимый порт");
            }
        }
    }
    /**
     * Метод для загрузки данных из файла в коллекцию
     * @param s - путь до файла
     */
    public void load(String s) {
        collectionManager.load(fileManager.load(s));
    }
}
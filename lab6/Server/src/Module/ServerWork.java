package Module;

import Manager.CollectionManager;
import Manager.CommandReader;
import Manager.FileManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import model.Product;
import org.apache.logging.log4j.LogManager;

/**
 * Класс отвечающий за отправку и получение сообщения, выбора конкретной команды
 * @autor Svytoq
 * @version 1.0
 */
public class ServerWork {
    /**
     * Поле исполнителя команд связанных с коллекцией
     */
    private CollectionManager collectionManager;
    /**
     * Поле исполнителя команд связанных с файлом
     */
    private FileManager fileManager;
    /**
     * Поле исполнителя команд связанных с вводом команд и их аргументов
     */
    private CommandReader commandReader;

    /**
     * Поле связанное с логикой работы команды при чтении из скрипта
     */
    private boolean interactiveMod;

    /**
     * Логгер
     */
    private Logger logger = Logger.getLogger("ServerLogger");

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     */
    public ServerWork(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
        this.commandReader = new CommandReader();
        this.interactiveMod = true;
    }

    /**
     Метод получающий от клиента конкретную команду
     */
    public CommandDescription receiveCommand(SelectionKey key) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        SocketChannel channel = (SocketChannel)key.channel();

        for(int available = channel.read(buffer); available > 0; available = channel.read(buffer)) {
        }

        byte[] buf = buffer.array();
        ObjectInputStream serialize = new ObjectInputStream(new ByteArrayInputStream(buf));
        CommandDescription command = (CommandDescription)serialize.readObject();
        serialize.close();
        buffer.clear();
        key.interestOps(4);
        return command;
    }

    /**
     Метод отправляющий клиенту результат исполнения
     */
    public void send(SelectionKey key, CommandDescription commandDescription) throws IOException {
        SocketChannel channel = (SocketChannel)key.channel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream toClient = new ObjectOutputStream(baos);
        switch(commandDescription.getCommand()) {
            case HELP:
                toClient.writeObject(this.collectionManager.help());
                break;
            case INFO:
                toClient.writeObject(this.collectionManager.info());
                break;
            case SHOW:
                toClient.writeObject(this.collectionManager.show());
                break;
            case ADD:
                commandDescription.getProduct().setId(this.collectionManager.getNewID());
                toClient.writeObject(this.collectionManager.add(commandDescription.getProduct()));
                break;
            case UPDATE:
                commandDescription.getProduct().setId(Integer.parseInt(commandDescription.getArgs()));
                toClient.writeObject(this.collectionManager.update(commandDescription.getProduct(), commandDescription.getProduct().getId()));
                break;
            case REMOVE_BY_ID:
                toClient.writeObject(this.collectionManager.removeByID(commandDescription.getArgs()));
                break;
            case CLEAR:
                toClient.writeObject(this.collectionManager.clear());
                break;
            case SAVE:
                toClient.writeObject(this.fileManager.save(this.collectionManager.getCollection()));
                break;
            case EXECUTE_SCRIPT:
                toClient.writeObject(scriptMod(commandDescription.getArgs()));
                break;
            case ADD_IF_MAX:
                commandDescription.getProduct().setId(this.collectionManager.getNewID());
                toClient.writeObject(this.collectionManager.addIfMax(commandDescription.getProduct()));
                break;
            case ADD_IF_MIN:
                commandDescription.getProduct().setId(this.collectionManager.getNewID());
                toClient.writeObject(this.collectionManager.addIfMin(commandDescription.getProduct()));
                break;
            case REMOVE_GREATER:
                commandDescription.getProduct().setId(this.collectionManager.getNewID());
                toClient.writeObject(this.collectionManager.removeGreater(commandDescription.getProduct()));
                break;
            case COUNT_LESS_THAN_OWNER:
                commandDescription.getProduct().setId(this.collectionManager.getNewID());
                toClient.writeObject(this.collectionManager.countLessThanOwner(commandDescription.getProduct()));
                break;
            case PRINT_DESCENDING:
                toClient.writeObject(this.collectionManager.printDescending());
                break;
            case PRINT_FIELD_ASCENDING_OWNER:
                toClient.writeObject(this.collectionManager.printFieldAscendingOwner());
                break;
            default:
                System.out.println("парам-парам");
        }

        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());

        for(int available = channel.write(buffer); available > 0; available = channel.write(buffer)) {
        }

        baos.close();
        toClient.close();
        key.interestOps(1);
    }

    /**
     Метод для обработки команд save и exit на сервере
     */
    public void serverMod() {
        String s = "";
            CommandDescription command = this.commandReader.readCommand();
            switch(command.getCommand()) {
                case HELP:
                case PRINT_FIELD_ASCENDING_OWNER:
                case PRINT_DESCENDING:
                case REMOVE_GREATER:
                case ADD_IF_MIN:
                case COUNT_LESS_THAN_OWNER:
                case ADD_IF_MAX:
                case EXECUTE_SCRIPT:
                case CLEAR:
                case REMOVE_BY_ID:
                case UPDATE:
                case ADD:
                case SHOW:
                case INFO:
                    System.out.println("На сервере поддерживаются только команды save и exit");
                    break;

                case SAVE:
                        System.out.println(this.fileManager.save(this.collectionManager.getCollection()));
                    break;
                case EXIT:
                        System.out.println(this.fileManager.save(this.collectionManager.getCollection()));
                        System.out.println("Программа сервера успешно завершена");
                        logger.info("Программа сервера успешно завершена");
                        System.exit(0);
                    break;
                default:
                        System.out.println("На сервере поддерживаются только команды save и exit..");

            }
    }

    /**
     Метод обработки команды скрипт
     */
    public String scriptMod(String file) {
        String s = "";
        interactiveMod = !commandReader.executeScript(file);
        while (commandReader.hasNextLine()) {
            CommandDescription command = this.commandReader.readCommand();
            Product product;
            switch (command.getCommand()) {
                case HELP:
                        s = s + this.collectionManager.help();
                    break;
                case INFO:
                        s = s + this.collectionManager.info();
                    break;
                case SHOW:
                        s = s + this.collectionManager.show();

                    break;
                case ADD:
                        product = this.commandReader.readProduct(0);
                        product.setId(this.collectionManager.getNewID());
                        s = s + this.collectionManager.add(product);
                    break;
                case UPDATE:
                        try {
                            int id = Integer.parseInt(command.getArgs());
                            product = commandReader.readProduct(0);
                            product.setId(id);
                            s = s + this.collectionManager.update(product, id);
                        } catch (IllegalArgumentException var9) {
                            s = s + "Неверный аргумент у команды, для справки введите команду info";
                        }
                    break;
                case REMOVE_BY_ID:
                        s = s + this.collectionManager.removeByID(command.getArgs());
                    break;
                case CLEAR:
                       s = s + this.collectionManager.clear();
                    break;
                case SAVE:
                        s = s + "Команда save не поддерживается с клиентского приложения";
                    break;
                case EXECUTE_SCRIPT:
                        s = s + "к сожалению в нашей программе нельзя вызвать скрипт в скрипте, это может привести к бесконечным циклам";
                    break;
                case ADD_IF_MAX:
                        product = this.commandReader.readProduct(0);
                        product.setId(this.collectionManager.getNewID());
                        s = s + this.collectionManager.addIfMax(product);
                    break;
                case ADD_IF_MIN:
                        product = this.commandReader.readProduct(0);
                        product.setId(this.collectionManager.getNewID());
                        s = s + this.collectionManager.addIfMin(product);
                    break;
                case REMOVE_GREATER:
                        product = this.commandReader.readProduct(0);
                        product.setId(this.collectionManager.getNewID());
                        s = s + this.collectionManager.removeGreater(product);
                    break;
                case COUNT_LESS_THAN_OWNER:
                        product = this.commandReader.readProduct(0);
                        product.setId(this.collectionManager.getNewID());
                        s = s + this.collectionManager.countLessThanOwner(product);
                    break;
                case PRINT_DESCENDING:
                        s = s + this.collectionManager.printDescending();

                    break;
                case PRINT_FIELD_ASCENDING_OWNER:
                        s = s + this.collectionManager.printFieldAscendingOwner();
                    break;
                case EXIT:
                        s = s + "Команда exit не поддерживается с клиентского приложения";
                    break;
                default:
                        s = s + this.commandReader.invalidCommand();
            }
        }
        this.commandReader.interactiveMod();
        this.interactiveMod = true;
        return s;
    }

    public Logger getLogger() {
        return logger;
    }
}

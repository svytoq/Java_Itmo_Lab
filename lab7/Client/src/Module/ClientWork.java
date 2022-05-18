package Module;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * Класс отвечающий за отправку и получение команд от сервера, выборки конкретной команды
 * @autor Svytoq
 * @version 1.0
 */
public class ClientWork {

    boolean access;

    /**
     * Поле исполнителя команд связанных с вводом команд и их аргументов
     */
    private final CommandReader commandReader = new CommandReader();

    /**
     * Основной метод клиента (отправляет необходимый объект на сервер)
     *
     * @param socket
     * @param command
     * @param login
     * @param password
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void work(Socket socket, String command, String login, String password) throws IOException, ClassNotFoundException {
        if (command.equals("reg")) {
            CommandDescription request = new CommandDescription(Command.REG, login, password);
            sendCommand(socket, request);
            getAnswer(socket);
        } else if (command.equals("sign")) {
            CommandDescription request = new CommandDescription(Command.SIGN, login, password);
            sendCommand(socket, request);
            getAnswer(socket);
        }
        if (access) {
            while (true) {
                CommandDescription commandDescription = commandReader.readCommand();
                choose(socket, commandDescription, login, password);
            }
        }
    }



    /**
     * Метод обрабатывает команды
     *
     * @throws IOException
     */
    public void choose(Socket socket, CommandDescription commandDescription, String login, String password) throws IOException {
        commandDescription.setLogin(login);
        commandDescription.setPassword(password);
        try {
            switch (commandDescription.getCommand()) {
                    case HELP:
                    case INFO:
                    case SHOW:
                    case CLEAR:
                    case PRINT_FIELD_ASCENDING_OWNER:
                    case PRINT_DESCENDING:
                        sendCommand(socket, commandDescription);
                        getAnswer(socket);
                        break;
                    case ADD:
                    case ADD_IF_MAX:
                    case ADD_IF_MIN:
                    case REMOVE_GREATER:
                    case COUNT_LESS_THAN_OWNER:
                        commandDescription.setProduct(commandReader.readProduct());
                        commandDescription.getProduct().setLoginP(login);
                        sendCommand(socket, commandDescription);
                        getAnswer(socket);
                        break;
                    case UPDATE:
                        try {
                            int id = Integer.parseInt(commandDescription.getArgs());
                            commandDescription.setProduct(commandReader.readProduct());
                            sendCommand(socket, commandDescription);
                            getAnswer(socket);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду info");
                        }
                        break;
                    case REMOVE_BY_ID:
                        try {
                            int id = Integer.parseInt(commandDescription.getArgs());
                            sendCommand(socket, commandDescription);
                            getAnswer(socket);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду info");
                        }
                        break;
                    case EXECUTE_SCRIPT:{
                        System.out.println("к сожалению не успел реализовать, не ругайте");
                    }
                        break;
                    case EXIT:
                        System.out.println("Программа клиента успешно завершена");
                        System.exit(0);
                        break;
                    case SAVE:
                        System.out.println("Команда save не поддерживается с клиентского приложения.");
                        break;
                    default:
                        commandReader.invalidCommand();
                }

        }catch (ArrayIndexOutOfBoundsException | ClassNotFoundException e){
            System.out.println("Отсутствует аргумент");
        }
    }

    /**
     * Метод отправляет команду на сервер
     *
     * @param socket
     * @param answer
     * @throws IOException
     */
    public void sendCommand(Socket socket, CommandDescription answer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream toServer = new ObjectOutputStream(baos);
        baos.flush();
        toServer.writeObject(answer);
        byte[] out = baos.toByteArray();
        socket.getOutputStream().write(out);
    }

    /**
     * Метод получает результат от сервера
     *
     * @param socket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void getAnswer(Socket socket) throws IOException, ClassNotFoundException {
        String answer;
        ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
        answer = (String) fromServer.readObject();
        switch (answer) {
            case "exit":
                System.exit(0);
            case "Авторизация прошла успешно":
                access = true;
                System.out.println("Вы успешно авторизованы. Введите help чтобы узнать список доступных команд.");
                break;
            default:
                System.out.println(answer);
                break;
        }
    }
}

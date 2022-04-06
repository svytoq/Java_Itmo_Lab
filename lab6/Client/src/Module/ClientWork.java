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

    /**
     * Поле исполнителя команд связанных с вводом команд и их аргументов
     */
    private final CommandReader commandReader = new CommandReader();

    /**
     * Метод обрабатывает команды
     *
     * @throws IOException
     */
    public void work(Socket socket) throws IOException {
        try {
            if (commandReader.hasNextLine()) {
                CommandDescription command = commandReader.readCommand();
                switch (command.getCommand()) {
                    case HELP:
                    case INFO:
                    case SHOW:
                    case EXECUTE_SCRIPT:
                    case CLEAR:
                    case PRINT_FIELD_ASCENDING_OWNER:
                    case PRINT_DESCENDING:
                        sendCommand(socket, command);
                        getAnswer(socket);
                        break;
                    case ADD:
                    case ADD_IF_MAX:
                    case ADD_IF_MIN:
                    case REMOVE_GREATER:
                    case COUNT_LESS_THAN_OWNER:
                        command.setProduct(commandReader.readProduct());
                        sendCommand(socket, command);
                        getAnswer(socket);
                        break;
                    case UPDATE:
                        try {
                            int id = Integer.parseInt(command.getArgs());
                            command.setProduct(commandReader.readProduct());
                            sendCommand(socket, command);
                            getAnswer(socket);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду info");
                        }
                        break;
                    case REMOVE_BY_ID:
                        try {
                            int id = Integer.parseInt(command.getArgs());
                            sendCommand(socket, command);
                            getAnswer(socket);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду info");
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
        if (answer.equals("exit")) {
            System.exit(0);
        } else {
            System.out.println(answer);
        }
    }
}

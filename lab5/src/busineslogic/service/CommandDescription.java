package busineslogic.service;

/**
 * Класс инкапсулирующий в себя тип команды и её аргументы
 * @autor Svytoq
 * @version 1.0
 */
public class CommandDescription {

    /** Поле типа команды */
    private final Command command;
    /** Поле аргументов */
    private final String args;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param command - тип команды
     * @param args - аргументы команды
     */
    public CommandDescription(Command command, String args) {
        this.command = command;
        this.args = args;
    }

    /**
     * Метод получения поля типа команды
     * @return command - тип команды
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Метод получения поля аргументов
     * @return command - аргументы
     */
    public String getArgs() {
        return args;
    }
}

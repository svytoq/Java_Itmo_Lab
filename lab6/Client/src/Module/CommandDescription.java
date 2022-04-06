package Module;

import Module.Command;
import model.Product;

import java.io.Serializable;

/**
 * Класс инкапсулирующий в себя тип команды и её аргументы
 * @autor Svytoq
 * @version 1.0
 */
public class CommandDescription implements Serializable {

    /** Поле типа команды */
    private Command command;
    /** Поле аргументов */
    private String args;

    private static final long serialVersionUID = 17L;

    private Product product;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param command - тип команды
     * @param args - аргументы команды
     */
    public CommandDescription(Command command, String args) {
        this.command = command;
        this.args = args;
    }

    public CommandDescription(Command command, Product product) {
        this.command = command;
        this.product = product;
    }

    public CommandDescription(Command command, String args, Product product) {
        this.command = command;
        this.args = args;
        this.product = product;
    }

    public CommandDescription() {
    }

    public void setCommand(Command command) {
        this.command = command;
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

    public void setArgs(String args) {
        this.args = args;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

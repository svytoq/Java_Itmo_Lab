package Module;

import java.io.Serializable;
import model.Product;

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
    /** Поле объекта коллекции */
    private Product product;

    private String login;

    private String password;

    /** константа для сериализации*/
    private static final long serialVersionUID = 17L;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param command - тип команды
     * @param args - аргументы команды
     */
    public CommandDescription(Command command, String args) {
        this.command = command;
        this.args = args;
    }

    public CommandDescription(Command command, String login, String password) {
        this.command = command;
        this.login = login;
        this.password = password;
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


    public Command getCommand() {
        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getArgs() {
        return this.args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
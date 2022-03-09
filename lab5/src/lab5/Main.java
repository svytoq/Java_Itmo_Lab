package lab5;

import busineslogic.service.*;
import model.Product;
import java.util.TreeSet;

/**
 * Входная точка входа в программу
 * @autor Svytoq
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler(new CollectionManager(new TreeSet<Product>()),new CommandReader(), new FileManager());
        if (args == null || args.length == 0){
            System.out.println("Не был передан путь к файлу при запуске программы");
        }else {
        commandHandler.load(args[0]);
        }
        commandHandler.doNextCommand();
    }
}

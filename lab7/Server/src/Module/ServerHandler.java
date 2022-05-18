package Module;


import Manager.CommandReader;
import model.Product;

import java.io.*;
import java.nio.channels.SelectionKey;
import java.security.NoSuchAlgorithmException;;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHandler implements Runnable{

    private CommandDescription commandDescription;
    private CollectionManager collectionManager;
    private BDActivity bdActivity;
    private ExecutorService poolSend;
    private SelectionKey key;
    private boolean interactiveMod;
    private CommandReader commandReader;
    Logger logger = Logger.getLogger("ServerLogger");
    /**
     * Метод регестрирует, авторизует пользователя или выполняет команду
     */

    public ServerHandler(CommandDescription commandDescription, CollectionManager manager, BDActivity bdActivity, ExecutorService poolSend, SelectionKey key){
    this.commandDescription = commandDescription;
    this.collectionManager = manager;
    this.bdActivity = bdActivity;
    this.poolSend = poolSend;
    this.key = key;
    this.commandReader = new CommandReader();
    this.interactiveMod = true;
    }

    @Override
    public void run() {
        try {
            if (commandDescription.getCommand() == Command.REG) {
                poolSend.submit(new ServerSender(key, bdActivity.registration(commandDescription)));
            } else if (commandDescription.getCommand() == Command.SIGN) {
                if (bdActivity.authorization(commandDescription)) {
                    logger.log(Level.INFO,"Пользователь с логином " + commandDescription.getLogin() + " успешно авторизован.");
                    poolSend.submit(new ServerSender(key, "Авторизация прошла успешно"));
                } else {
                    logger.log(Level.INFO,"Пользователь ввел не верный пароль");
                    poolSend.submit(new ServerSender(key, "Логин или пароль введены неверно"));
                }
            } else {
                if (bdActivity.authorization(commandDescription)) {
                    switch (commandDescription.getCommand()) {
                        case HELP:
                            collectionManager.help(poolSend, key);
                            break;
                        case INFO:
                            collectionManager.info(poolSend, key);
                            break;
                        case SHOW:
                        case PRINT_DESCENDING:
                            collectionManager.show(poolSend, key);
                            break;
                        case ADD:
                            commandDescription.getProduct().setId(bdActivity.getSQLId());
                            collectionManager.add(commandDescription.getProduct(), poolSend, key, bdActivity, commandDescription.getLogin());
                            break;
                        case UPDATE:
                            commandDescription.getProduct().setId(Integer.parseInt(commandDescription.getArgs()));
                            collectionManager.update(commandDescription.getProduct(), poolSend, key, bdActivity, commandDescription.getLogin());
                            break;
                        case REMOVE_BY_ID:
                            collectionManager.removeByID(commandDescription.getArgs(), poolSend, key, bdActivity, commandDescription.getLogin());
                            break;
                        case CLEAR:
                            collectionManager.clear(key, poolSend, bdActivity, commandDescription.getLogin());
                            break;
                        case SAVE:
                            //
                            break;
                        case EXECUTE_SCRIPT:
                            //poolSend.submit(new ServerSender(key, scriptMod(commandDescription.getArgs())));
                            break;
                        case ADD_IF_MAX:
                            commandDescription.getProduct().setId(bdActivity.getSQLId());
                            collectionManager.addIfMax(commandDescription.getProduct(), key, poolSend, bdActivity, commandDescription.getLogin());
                            break;
                        case ADD_IF_MIN:
                            commandDescription.getProduct().setId(bdActivity.getSQLId());
                            collectionManager.addIfMin(commandDescription.getProduct(), key, poolSend, bdActivity, commandDescription.getLogin());
                            break;
                        case REMOVE_GREATER:
                            commandDescription.getProduct().setId(bdActivity.getSQLId());
                            collectionManager.removeGreater(commandDescription.getProduct(), key, poolSend, bdActivity, commandDescription.getLogin());
                            break;
                        case COUNT_LESS_THAN_OWNER:
                            commandDescription.getProduct().setId(bdActivity.getSQLId());
                            collectionManager.countLessThanOwner(commandDescription.getProduct(), key, poolSend);
                            break;
                        case PRINT_FIELD_ASCENDING_OWNER:
                            collectionManager.printFieldAscendingOwner(key, poolSend);
                            break;
                        default:
                            logger.log(Level.INFO,"парам-парам");
                    }
                    logger.log(Level.INFO,"Обработана команда " + commandDescription.getCommand().toString());
                }
            }
        } catch (Exception e) {
            //
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
                       // s = s + this.collectionManager.help();
                        break;
                    case INFO:
                        // s = s + this.collectionManager.info();
                        break;
                    case SHOW:
                        //s = s + this.collectionManager.show();

                        break;
                    case ADD:
                      //  product = this.commandReader.readProduct(0);
                        // product.setId(this.collectionManager.getNewID());
                        //s = s + this.collectionManager.add(product);
                        break;
                    case UPDATE:
                     //   try {
                       //     int id = Integer.parseInt(command.getArgs());
                         //   product = commandReader.readProduct(0);
                           // product.setId(id);
                            //s = s + this.collectionManager.update(product, id);
                        //} catch (IllegalArgumentException var9) {
                          //  s = s + "Неверный аргумент у команды, для справки введите команду info";
                        //}
                        break;
                    case REMOVE_BY_ID:
                        //s = s + this.collectionManager.removeByID(command.getArgs());
                        break;
                    case CLEAR:
                       // s = s + this.collectionManager.clear();
                        break;
                    case SAVE:
                        // s = s + "Команда save не поддерживается с клиентского приложения";
                        break;
                    case EXECUTE_SCRIPT:
                        s = s + "к сожалению в нашей программе нельзя вызвать скрипт в скрипте, это может привести к бесконечным циклам";
                        break;
                    case ADD_IF_MAX:
                    //    product = this.commandReader.readProduct(0);
                      //  product.setId(this.collectionManager.getNewID());
                      //  s = s + this.collectionManager.addIfMax(product);
                        break;
                    case ADD_IF_MIN:
                     //   product = this.commandReader.readProduct(0);
                     //   product.setId(this.collectionManager.getNewID());
                     //   s = s + this.collectionManager.addIfMin(product);
                        break;
                    case REMOVE_GREATER:
                       // product = this.commandReader.readProduct(0);
                     //   product.setId(this.collectionManager.getNewID());
                       // s = s + this.collectionManager.removeGreater(product);
                        break;
                    case COUNT_LESS_THAN_OWNER:
                      //  product = this.commandReader.readProduct(0);
                      //  product.setId(this.collectionManager.getNewID());
                      //  s = s + this.collectionManager.countLessThanOwner(product);
                        break;
                    case PRINT_DESCENDING:
                        s = s + this.collectionManager.printDescending();

                        break;
                    case PRINT_FIELD_ASCENDING_OWNER:
                      //  s = s + this.collectionManager.printFieldAscendingOwner();
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
}
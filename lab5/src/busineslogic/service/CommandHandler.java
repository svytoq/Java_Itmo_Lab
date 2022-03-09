package busineslogic.service;


/**
 * Класс управляющей всей программой, содержит в себе информацию о всех исполнителях команд
 * переправляет команду определенному исполнителю
 * @autor Svytoq
 * @version 1.0
 */
public class CommandHandler {

    /**
     * Поле исполнителя команд связанных с коллекцией
     */
    private final CollectionManager collectionManager;

    /**
     * Поле связанное с логикой работы команды при чтении из скрипта
     */
    boolean scriptMod = false;

    /**
     * Поле исполнителя команд связанных с вводом команд и их аргументов
     */
    private final CommandReader commandReader;
    /**
     * Поле исполнителя команд связанных с файлом
     */
    private final FileManager fileManager;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param collectionManager - исполнитель команд связанных с коллекцией
     * @param commandReader     - исполнитель команд связанных с вводом команд и их аргументов
     * @param fileManager       - исполнитель команд связанных с файлом
     */
    public CommandHandler(CollectionManager collectionManager, CommandReader commandReader, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.commandReader = commandReader;
        this.fileManager = fileManager;
    }

    /**
     * Метод загрузки коллекции из файла в память программы
     *
     * @param s - путь до файла, который передается при запуске программы с помощью аргументов командной строки
     */
    public void load(String s) {
        collectionManager.load(fileManager.load(s));
    }


    /**
     * Метод вызывающий чтение команд и аргументов и их дальнейшее выполнение у конкретных исполнителей
     * работает пока не вызовешь команду exit
     */
    public void doNextCommand() {
        System.out.println("Программа готова к использованию, для справки введите команду help");
        while (true) {
            if (commandReader.hasNextLine()) {
                CommandDescription command = commandReader.readCommand();
                switch (command.getCommand()) {
                    case HELP:
                        commandReader.help();
                        break;
                    case INFO:
                        collectionManager.info();
                        break;
                    case SHOW:
                        collectionManager.show();
                        break;
                    case ADD:
                        collectionManager.add(commandReader.readProduct(collectionManager.getNewID()));
                        break;
                    case UPDATE:
                        try {
                            int id = Integer.parseInt(command.getArgs());
                            if (collectionManager.checkId(id)) {
                                collectionManager.update(commandReader.readProduct(id), id);
                            } else {
                                System.out.println("Элемента с таким id нету в коллекции");
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду info");
                        }
                        break;
                    case REMOVE_BY_ID:
                        try {
                            int id = Integer.parseInt(command.getArgs());
                            collectionManager.removeByID(id);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду info");
                        }
                        break;
                    case CLEAR:
                        collectionManager.clear();
                        break;
                    case SAVE:
                        fileManager.save(collectionManager.getCollection());
                        break;
                    case EXECUTE_SCRIPT:
                        if (scriptMod){
                            System.out.println("к сожалению в нашей программе нельзя вызвать скрипт в скрипте, это может привести к бесконечным циклам");
                        }else{
                            scriptMod = commandReader.executeScript(command.getArgs());
                        }
                        break;
                    case EXIT:
                        System.out.println("Программа успешно завершена");
                        System.exit(0);
                        break;
                    case ADD_IF_MAX:
                        collectionManager.addIfMax(commandReader.readProduct(collectionManager.getNewID()));
                        break;
                    case ADD_IF_MIN:
                        collectionManager.addIfMin(commandReader.readProduct(collectionManager.getNewID()));
                        break;
                    case REMOVE_GREATER:
                        collectionManager.removeGreater(commandReader.readProduct(collectionManager.getNewID()));
                        break;
                    case COUNT_LESS_THAN_OWNER:
                        collectionManager.countLessThanOwner(commandReader.readPerson());
                        break;
                    case PRINT_DESCENDING:
                        collectionManager.printDescending();
                        break;
                    case PRINT_FIELD_ASCENDING_OWNER:
                        collectionManager.printFieldAscendingOwner();
                        break;
                    default:
                        commandReader.invalidCommand();
                }
            }else {
            commandReader.interactiveMod();
            scriptMod = false;
            }
        }
    }
}
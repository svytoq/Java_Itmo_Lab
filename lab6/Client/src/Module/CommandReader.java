package Module;

import Module.Command;
import Module.CommandDescription;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;
import java.util.Scanner;

/**
 * Класс отвечающий за чтение команд и их аргументов
 * @autor Svytoq
 * @version 1.0
 */
public class CommandReader {

    /** Поле типа команды */
    private Command command;
    /** Поле названия команды */
    private String userCommand;
    /** Поле типа команды и ее аргументы */
    private String[] finalUserCommand = new  String[2];

    /** Сканер для чтения информации из командной строки*/
    Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор - создание нового объекта
     */
    public CommandReader() {
    }


    /**
     * Метод для работы со скриптом
     * @param t - путь к файлу
     * @return boolean, вошла ли программа в режим скрипта
     */
    public boolean executeScript(String t) {
        File scriptFile = null;
        FileReader fileReader = null;
        boolean flag = false;
        try {
            scriptFile = new File(t);
            fileReader = new FileReader(scriptFile);
            scanner = new Scanner(fileReader);
            flag = true;

        } catch (FileNotFoundException e) {
            System.out.println("файл не найден");
        }
        return flag;
    }

    /**
     * Метод для проверки при работе со скриптом
     * @return boolean, остались ли строки в файле
     */
    public boolean hasNextLine(){
       return scanner.hasNextLine();
    }

    /**
     * Метод для возращения к работе интерактивного режима
     */
    public void interactiveMod(){
        scanner = new Scanner(System.in);
    }

    /**
     * Метод для чтения команды из командной строки
     * @return возвращает объект класса CommandDescription, тип команды и ее аргументы
     */
    public CommandDescription readCommand() {
        userCommand = scanner.nextLine();
        finalUserCommand = userCommand.trim().split(" ", 2);
        try {
            command = Command.valueOf(finalUserCommand[0].toUpperCase(Locale.ROOT));
        } catch ( IllegalArgumentException e){
            command = Command.INVALID_COMMAND;
        }
        if (finalUserCommand.length > 1) {
            return new CommandDescription(command, finalUserCommand[1]);
        }else {
            return new CommandDescription(command, "");
        }
    }

    /**
     * Метод для чтения объекта Product из командной строки
     * @return возвращает объект класса Product
     */
    public Product readProduct(){
        String name =readNames();
        Coordinates coordinates = readCoordinates();
        java.time.LocalDateTime creationDate = java.time.LocalDateTime.now();
        Double price = readPrice();
        String partNumber = readPartNumber();
        double manufactureCost = readManufactureCost();
        UnitOfMeasure unitOfMeasure = readUnitOfMeasure();
        Person owner = readPerson();
        return new Product(0,name,coordinates, creationDate, price,partNumber, manufactureCost, unitOfMeasure, owner);
    }

    /**
     * Вспомогательный метод для чтения объекта Coordinates из командной строки
     * @return возвращает объект класса Coordinates
     */
    public Coordinates readCoordinates(){
        Double x = null;
        Long y = 666l;
        while (x == null){
            System.out.println("Введите x, x должно быть вещественным числом");
            try {
                x = Double.parseDouble(scanner.nextLine());
            }catch (IllegalArgumentException e){
                System.out.println("x должно быть вещественным числом");
            }
        }
        while (y >665){
            System.out.println("Введите y, y должно быть целым числом, меньшим чем 666");
            try {
                y = Long.parseLong(scanner.nextLine());
            }catch (IllegalArgumentException e){
                System.out.println("y должно быть целым числом, меньшим чем 666");
            }
        }
        return new Coordinates(x,y);
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     * @return возвращает объект класса String
     */
    public String readNames(){
        String name ="";
        while (name.equals("")){
            System.out.println("Введите name, поле не может быть пустой строкой");
            name = scanner.nextLine();
        }
        return name;
    }

    /**
     * Вспомогательный метод для чтения цены из командной строки
     * @return возвращает цену Double
     */
    public Double readPrice(){
        Double price = -1D;
        do {
            System.out.println("введите price, если хотите,чтоб значение поля было null - нажмите Enter, иначе значение поля должно быть вещественным числом больше 0");
            String s = scanner.nextLine();
            if (s.equals("")){
                price = null;

            }else {
                try {
                    price = Double.parseDouble(s);
                }catch (IllegalArgumentException e){
                    System.out.println("price - обязано быть вещественным числом >0 или null");
                }
            }
        }while (!(price == null) && (price <= 0));
        return price;
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     * @return возвращает объект класса String
     */
    public String readPartNumber(){
        String partNumber = "";

            System.out.println("Введите partNumber, если хотите, чтоб поле было равно null - нажмите Enter, иначе он должен быть строкой");
            String s = scanner.nextLine();
            if (s.equals("")){
                partNumber = null;
            }else {
                partNumber = s;
            }
        return partNumber;
    }

    /**
     * Вспомогательный метод для чтения manufacture из командной строки
     * @return возвращает объект manufacture Double
     */
    public double readManufactureCost(){
        double manufactureCost = 0;
        boolean flag = false;
        while (!flag){
            System.out.println("Введите manufactureCost, оно обязано быть вещественным числом");
            try {
                manufactureCost = Double.parseDouble(scanner.nextLine());
                flag = true;
            }catch (IllegalArgumentException e){
                System.out.println("manufactureCost обязано быть вещественным числом");
            }
        }
        return manufactureCost;
    }

    /**
     * Вспомогательный метод для чтения объекта UnitOfMeasure из командной строки
     * @return возвращает объект класса UnitOfMeasure
     */
    public UnitOfMeasure readUnitOfMeasure(){
        UnitOfMeasure unitOfMeasure = null;
        while (unitOfMeasure == null){
            try {
                System.out.println("Введите unitOfMeasure, значение поля может быть равно: KILOGRAMS, METERS, CENTIMETERS, PCS");
                unitOfMeasure = UnitOfMeasure.valueOf(scanner.nextLine());
            }catch (IllegalArgumentException e){
                System.out.println("значение поля может быть равно: KILOGRAMS, METERS, CENTIMETERS, PCS");
            }
        }
        return unitOfMeasure;
    }

    /**
     * Вспомогательный метод для чтения объекта Person из командной строки
     * @return возвращает объект класса Person
     */
    public Person readPerson(){
        String name = readNames();
        float weight = readWeight();
        Color color = readColor();
        Location location = readLocation();
        return new Person(name,weight,color,location );
    }

    /**
     * Вспомогательный метод для чтения Weight из командной строки
     * @return возвращает объект weight Double
     */
    public float readWeight(){
        float weight = 0;
        while (weight <=0){
        try {
            System.out.println("Введите weight, оно обязано быть вещественным числом больше 0");
            weight = Float.parseFloat(scanner.nextLine());
        }catch (IllegalArgumentException e){
            System.out.println("weight обязано быть вещественным числом больше 0");
        }
        }
        return weight;
    }

    /**
     * Вспомогательный метод для чтения Color из командной строки
     * @return возвращает объект Color
     */
    public Color readColor(){
        Color color = null;
        while (color == null){
            try {
                System.out.println("Введите color, значение поля может быть равно: RED, BLACK, YELLOW, BROWN");
                color = Color.valueOf(scanner.nextLine());
            }catch (IllegalArgumentException e){
                System.out.println("значение поля может быть равно: RED, BLACK, YELLOW, BROWN");
            }
        }
        return color;
    }

    /**
     * Вспомогательный метод для чтения объекта Location из командной строки
     * @return возвращает объект класса Location
     */
    public Location readLocation(){
        int x = 0;
        boolean flagX = false;
        float y = 0;
        boolean flagY = false;
        Long z = 0L;
        boolean flagZ = false;
        String name = "";

        while (!flagX){
            try {
                System.out.println("Введите x, оно обязано быть целым числом");
            x = Integer.parseInt(scanner.nextLine());
            flagX = true;
            }catch (IllegalArgumentException e){
                System.out.println("x обязано быть целым числом");
            }
        }
        while (!flagY){
            try {
                System.out.println("Введите y, оно обязано быть вещественным числом");
                y = Float.parseFloat(scanner.nextLine());
                flagY = true;
            }catch (IllegalArgumentException e){
                System.out.println("y обязано быть вещественным числом");
            }
        }
        while (!flagZ){
            try {
                System.out.println("Введите z, оно обязано быть целым числом");
                z = Long.parseLong(scanner.nextLine());
                flagZ = true;
            }catch (IllegalArgumentException e){
                System.out.println("z обязано быть целым числом");
            }
        }

        do {
            System.out.println("Введите name, если хотите чтоб name было равно null, то нажмите Enter");
            String s = scanner.nextLine();
            if (s.equals("")){
                name = null;
            }else {
                name = s;
            }
        }while (!(name == null) && name.length() > 967);
        return new Location(x,y,z,name);
    }
    /**
     * Mетод для обработки нелегитимной команды
     */
    public void invalidCommand(){
        System.out.println("такой команды не существует, для справки введите команду help");
    }
    /**
     * Mетод для обработки команды help
     */
    public void help(){
        System.out.println("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "count_less_than_owner owner : вывести количество элементов, значение поля owner которых меньше заданного\n" +
                "print_descending : вывести элементы коллекции в порядке убывания\n" +
                "print_field_ascending_owner : вывести значения поля owner всех элементов в порядке возрастания");
    }
}

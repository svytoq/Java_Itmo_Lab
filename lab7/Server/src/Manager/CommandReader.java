package Manager;

import Module.Command;
import Module.CommandDescription;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Scanner;
import model.Color;
import model.Coordinates;
import model.Location;
import model.Person;
import model.Product;
import model.UnitOfMeasure;

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
    private String[] finalUserCommand = new String[2];
    /** Сканер для чтения информации из командной строки*/
    Scanner scanner;

    /**
     * Конструктор - создание нового объекта
     */
    public CommandReader() {
        this.scanner = new Scanner(System.in);
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
            this.scanner = new Scanner(fileReader);
            flag = true;
        } catch (FileNotFoundException var6) {
            System.out.println("файл не найден");
        }

        return flag;
    }

    /**
     * Метод для проверки при работе со скриптом
     * @return boolean, остались ли строки в файле
     */
    public boolean hasNextLine() {
        return this.scanner.hasNextLine();
    }

    /**
     * Метод для возращения к работе интерактивного режима
     */
    public void interactiveMod() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Метод для чтения команды из командной строки
     * @return возвращает объект класса CommandDescription, тип команды и ее аргументы
     */
    public CommandDescription readCommand() {
        this.userCommand = this.scanner.nextLine();
        this.finalUserCommand = this.userCommand.trim().split(" ", 2);

        try {
            this.command = Command.valueOf(this.finalUserCommand[0].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException var2) {
            this.command = Command.INVALID_COMMAND;
        }

        return this.finalUserCommand.length > 1 ? new CommandDescription(this.command, this.finalUserCommand[1]) : new CommandDescription(this.command, "");
    }

    /**
     * Метод для чтения объекта Product из командной строки
     * @param id - id нового объекта
     * @return возвращает объект класса Product
     */
    public Product readProduct(int id) {
        String name = this.readNames();
        Coordinates coordinates = this.readCoordinates();
        LocalDateTime creationDate = LocalDateTime.now();
        Double price = this.readPrice();
        String partNumber = this.readPartNumber();
        double manufactureCost = this.readManufactureCost();
        UnitOfMeasure unitOfMeasure = this.readUnitOfMeasure();
        Person owner = this.readPerson();
        String loginP = "aa";
        return new Product(id, name, coordinates, creationDate, price, partNumber, manufactureCost, unitOfMeasure, owner, loginP);
    }

    /**
     * Вспомогательный метод для чтения объекта Coordinates из командной строки
     * @return возвращает объект класса Coordinates
     */
    public Coordinates readCoordinates() {
        Double x = null;
        Long y = 666L;

        while(x == null) {
            try {
                x = Double.parseDouble(this.scanner.nextLine());
            } catch (IllegalArgumentException var5) {

            }
        }

        while(y > 665L) {

            try {
                y = Long.parseLong(this.scanner.nextLine());
            } catch (IllegalArgumentException var4) {
            }
        }

        return new Coordinates(x, y);
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     * @return возвращает объект класса String
     */
    public String readNames() {
        String name;
        for(name = ""; name.equals(""); name = this.scanner.nextLine()) {

        }

        return name;
    }

    /**
     * Вспомогательный метод для чтения цены из командной строки
     * @return возвращает цену Double
     */
    public Double readPrice() {
        Double price = -1.0D;

        do {
            String s = this.scanner.nextLine();
            if (s.equals("")) {
                price = null;
            } else {
                try {
                    price = Double.parseDouble(s);
                } catch (IllegalArgumentException var4) {
               }
            }
        } while(price != null && price <= 0.0D);

        return price;
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     * @return возвращает объект класса String
     */
    public String readPartNumber() {
        String partNumber = "";
        String s = this.scanner.nextLine();
        if (s.equals("")) {
            partNumber = null;
        } else {
            partNumber = s;
        }

        return partNumber;
    }

    /**
     * Вспомогательный метод для чтения manufacture из командной строки
     * @return возвращает объект manufacture Double
     */
    public double readManufactureCost() {
        double manufactureCost = 0.0D;
        boolean flag = false;
        while(!flag) {
            try {
                manufactureCost = Double.parseDouble(this.scanner.nextLine());
                flag = true;
            } catch (IllegalArgumentException var5) {
            }
        }

        return manufactureCost;
    }

    /**
     * Вспомогательный метод для чтения объекта UnitOfMeasure из командной строки
     * @return возвращает объект класса UnitOfMeasure
     */
    public UnitOfMeasure readUnitOfMeasure() {
        UnitOfMeasure unitOfMeasure = null;

        while(unitOfMeasure == null) {
            try {
                unitOfMeasure = UnitOfMeasure.valueOf(this.scanner.nextLine());
            } catch (IllegalArgumentException var3) {
            }
        }

        return unitOfMeasure;
    }


    /**
     * Вспомогательный метод для чтения объекта Person из командной строки
     * @return возвращает объект класса Person
     */
    public Person readPerson() {
        String name = this.readNames();
        float weight = this.readWeight();
        Color color = this.readColor();
        Location location = this.readLocation();
        return new Person(name, weight, color, location);
    }

    /**
     * Вспомогательный метод для чтения Weight из командной строки
     * @return возвращает объект weight Double
     */
    public float readWeight() {
        float weight = 0.0F;

        while(weight <= 0.0F) {
            try {
                weight = Float.parseFloat(this.scanner.nextLine());
            } catch (IllegalArgumentException var3) {
             }
        }

        return weight;
    }

    /**
     * Вспомогательный метод для чтения Color из командной строки
     * @return возвращает объект Color
     */
    public Color readColor() {
        Color color = null;

        while(color == null) {
            try {
                color = Color.valueOf(this.scanner.nextLine());
            } catch (IllegalArgumentException var3) {
            }
        }

        return color;
    }

    /**
     * Вспомогательный метод для чтения объекта Location из командной строки
     * @return возвращает объект класса Location
     */
    public Location readLocation() {
        int x = 0;
        boolean flagX = false;
        float y = 0.0F;
        boolean flagY = false;
        Long z = 0L;
        boolean flagZ = false;
        String name = "";

        while(!flagX) {
            try {
                x = Integer.parseInt(this.scanner.nextLine());
                flagX = true;
            } catch (IllegalArgumentException var11) {

            }
        }

        while(!flagY) {
            try {
                y = Float.parseFloat(this.scanner.nextLine());
                flagY = true;
            } catch (IllegalArgumentException var10) {

            }
        }

        while(!flagZ) {
            try {
                z = Long.parseLong(this.scanner.nextLine());
                flagZ = true;
            } catch (IllegalArgumentException var9) {
            }
        }

        do {
            String s = this.scanner.nextLine();
            if (s.equals("")) {
                name = null;
            } else {
                name = s;
            }
        } while(name != null && name.length() > 967);

        return new Location(x, y, z, name);
    }

    /**
     * Mетод для обработки нелегитимной команды
     */
    public String invalidCommand() {
        return "такой команды не существует, для справки введите команду help";
    }


    /**
     * Mетод для обработки команды help
     */
    public void help() {
        System.out.println("help : вывести справку по доступным командам\ninfo : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\nshow : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\nadd {element} : добавить новый элемент в коллекцию\nupdate id {element} : обновить значение элемента коллекции, id которого равен заданному\nremove_by_id id : удалить элемент из коллекции по его id\nclear : очистить коллекцию\nsave : сохранить коллекцию в файл\nexecute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\nexit : завершить программу (без сохранения в файл)\nadd_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\nadd_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\nremove_greater {element} : удалить из коллекции все элементы, превышающие заданный\ncount_less_than_owner owner : вывести количество элементов, значение поля owner которых меньше заданного\nprint_descending : вывести элементы коллекции в порядке убывания\nprint_field_ascending_owner : вывести значения поля owner всех элементов в порядке возрастания");
    }
}

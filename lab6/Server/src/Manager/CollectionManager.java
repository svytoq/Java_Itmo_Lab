package Manager;


import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import model.Product;

/**
 * Класс для работы с коллекцией
 * @autor Svytoq
 * @version 1.0
 */
public class CollectionManager {

    /** Поле коллекция */
    private TreeSet<Product> collection;
    /** Поле дата создания */
    private Date creationDate;
    /** Поле файл, в котором хранится коллекция */
    private File file;

    /**
     * Конструктор - создание объекта
     * @param collection - коллекция для сохранения объектов
     */
    public CollectionManager(TreeSet<Product> collection) {
        this.collection = collection;
        this.creationDate = new Date();
    }

    /**
     * Mетод для обработки команды help
     */
    public String help() {
        return "help : вывести справку по доступным командам\ninfo : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\nshow : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\nadd {element} : добавить новый элемент в коллекцию\nupdate id {element} : обновить значение элемента коллекции, id которого равен заданному\nremove_by_id id : удалить элемент из коллекции по его id\nclear : очистить коллекцию\nsave : сохранить коллекцию в файл\nexecute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\nexit : завершить программу (без сохранения в файл)\nadd_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\nadd_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\nremove_greater {element} : удалить из коллекции все элементы, превышающие заданный\ncount_less_than_owner owner : вывести количество элементов, значение поля owner которых меньше заданного\nprint_descending : вывести элементы коллекции в порядке убывания\nprint_field_ascending_owner : вывести значения поля owner всех элементов в порядке возрастания";
    }

    /**
     * Показывает объекты в коллекции со всеми полями
     */
    public String show() {
        String s = "";
        Product product;
        if (this.collection != null && !this.collection.isEmpty()) {
            for(Iterator var2 = this.collection.iterator(); var2.hasNext(); s = s + product.toString() + "\n") {
                product = (Product)var2.next();
            }
        } else {
            s = "В коллекции нет элементов";
        }

        return s;
    }

    /**
     * Показывает информацию о коллекции: тип, дата создания, размер
     */
    public String info() {
        return "Тип коллекции: " + this.collection.getClass().toString() + " дата создания:" + this.creationDate + " размер: " + this.collection.size();
    }


    /**
     * Добавляет новый элемент в коллекцию
     * @param product: объект класса Product
     */
    public String add(Product product) {
        this.collection.add(product);
        return "Объект успешно добавлен";
    }


    /**
     * Метод получения нового Id
     * @return Возвращает новый уникальный Id
     */
    public int getNewID() {
        if (this.collection != null && !this.collection.isEmpty()) {
            int i = 1;

            Product product;
            for(Iterator var2 = this.collection.iterator(); var2.hasNext(); i = Math.max(i, product.getId())) {
                product = (Product)var2.next();
            }

            return i + 1;
        } else {
            return 1;
        }
    }

    /**
     * Метод проверки нахождение объекта с заданным Id в коллекции
     * @param id Id
     * @return true если объект с таким id есть, иначе false
     */
    public boolean checkId(int id) {
        boolean flag = false;
        Iterator var3 = this.collection.iterator();

        while(var3.hasNext()) {
            Product product = (Product)var3.next();
            if (id == product.getId()) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * метод для обновления объекта с заданным Id
     * @param product - новый объект
     * @param id - id этого объекта
     */
    public String update(Product product, int id) {
        boolean flag = false;
        String s = "";
        Iterator var5 = this.collection.iterator();

        while(var5.hasNext()) {
            Product product1 = (Product)var5.next();
            if (product1.getId() == id) {
                this.collection.remove(product1);
                this.collection.add(product);
                s = "Элемент с id = " + id + " успешно обновлён";
                flag = true;
                break;
            }
        }

        if (!flag) {
            s = "Элемента с таким id нету в коллекции";
        }

        return s;
    }

    /**
     * Метод для удаления объекта с заданным id
     * @param argument - id объекта, который надо удалить
     */
    public String removeByID(String argument) {
        int id = Integer.parseInt(argument);
        boolean flag = false;
        String s = "";
        Iterator var5 = this.collection.iterator();

        while(var5.hasNext()) {
            Product product1 = (Product)var5.next();
            if (product1.getId() == id) {
                this.collection.remove(product1);
                s = "Элемент с id = " + id + " успешно удален";
                flag = true;
                break;
            }
        }

        if (!flag) {
            s = "элемента с таким id не существует";
        }

        return s;
    }

    /**
     * Удаляет все объекты из коллекции
     */
    public String clear() {
        this.collection.clear();
        return "элементы коллекции успешно удалены";
    }

    /**
     * Метод для добавления объекта, если он больше наибольшего объекта в коллекции
     * @param product - объект для добавления
     */
    public String addIfMax(Product product) {
        String s = "";
        if (product.compareTo((Product)this.collection.first()) > 0) {
            this.collection.add(product);
            s = "Элемент успешно добавлен в коллекцию";
        } else {
            s = "Элемент меньше чем наибольший элемент в коллекции";
        }

        return s;
    }

    /**
     * Метод для добавления объекта, если он меньше наименьшего объекта в коллекции
     * @param product - объект для добавления
     */
    public String addIfMin(Product product) {
        String s = "";
        if (product.compareTo((Product)this.collection.last()) < 0) {
            this.collection.add(product);
            s = "Элемент успешно добавлен в коллекцию";
        } else {
            s = "Элемент больше чем наименьший элемент в коллекции";
        }

        return s;
    }

    /**
     * Метод для удаления объектов, если они больше заданного объекта
     * @param product - объект для сравнения
     */
    public String removeGreater(Product product) {
        this.collection.removeIf((product1) -> {
            return product1.compareTo(product) > 0;
        });
        return "Элементы удалены.";
    }

    /**
     * Метод для вывода количество элементов, значение поля owner которых меньше заданного
     * @param product - объект для сравнения
     */
    public String countLessThanOwner(Product product) {
        int counter = 0;
        Iterator var3 = this.collection.iterator();

        while(var3.hasNext()) {
            Product product1 = (Product)var3.next();
            if (product1.getOwner().compareTo(product.getOwner()) < 0) {
                ++counter;
            }
        }

        return "Количество элементов меньше заданного: " + counter;
    }

    /**
     * Метод для вывода элементов в порядке убывания
     * так как используемая коллекция TreeSet, которая хранит объекты в натуральном порядке,
     * то ничем не отличается от команды show
     */
    public String printDescending() {
        String s = "";
        Product product1;
        if (this.collection != null && !this.collection.isEmpty()) {
            for(Iterator var2 = this.collection.iterator(); var2.hasNext(); s = s + product1.toString() + "\n") {
                product1 = (Product)var2.next();
            }
        } else {
            s = "В коллекции нет элементов";
        }

        return s;
    }
    /**
     * Метод для вывода поля owner в порядке возрастания
     */
    public String printFieldAscendingOwner() {
        String s = "";
        if (!this.collection.isEmpty()) {
            Product product1;
            for(Iterator var2 = this.collection.iterator(); var2.hasNext(); s = product1.getOwner().toString() + "\n" + s) {
                product1 = (Product)var2.next();
            }

            System.out.println(s);
        } else {
            s = "В коллекции нет элементов";
        }

        return s;
    }

    /**
     * Метод для загрузки коллекции
     * @param collection1 - коллекция загруженная из файла
     */
    public void load(TreeSet<Product> collection1) {
        if (collection1 != null) {
            this.collection.addAll(collection1);
        }

    }

    /**
     * Метод для возвращения коллекции
     * @return collection - коллекция
     */
    public TreeSet<Product> getCollection() {
        return this.collection;
    }
}

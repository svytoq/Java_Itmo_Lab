package Module;


import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import Module.BDActivity;
import Module.ServerConnection;
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
    Logger logger = Logger.getLogger("ServerLogger");

    /**
     * Конструктор - создание объекта
     * @param collection - коллекция для сохранения объектов
     */
    public CollectionManager(TreeSet<Product> collection) {
        this.collection = collection;
        this.creationDate = new Date();
    }

    public synchronized String loadToCol(String file, BDActivity bdActivity) throws ClassNotFoundException {
        try {
            collection = bdActivity.loadFromSQL(file);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Сервер не подключился к БД");
            return "Ошибка сервер не может подключиться к БД (вероятно что-то с БД)";
        } catch (IOException e) {
            System.out.println("Сервер не подключился к БД");
            return "Файл с данными БД не найден";
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }


    /**
     * Mетод для обработки команды help
     */
    public void help(ExecutorService poolSend, SelectionKey key) {
        Runnable help = () -> {
            poolSend.submit(new ServerSender(key, "help : вывести справку по доступным командам\ninfo : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\nshow : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\nadd {element} : добавить новый элемент в коллекцию\nupdate id {element} : обновить значение элемента коллекции, id которого равен заданному\nremove_by_id id : удалить элемент из коллекции по его id\nclear : очистить коллекцию\nsave : сохранить коллекцию в файл\nexecute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\nexit : завершить программу (без сохранения в файл)\nadd_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\nadd_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\nremove_greater {element} : удалить из коллекции все элементы, превышающие заданный\ncount_less_than_owner owner : вывести количество элементов, значение поля owner которых меньше заданного\nprint_descending : вывести элементы коллекции в порядке убывания\nprint_field_ascending_owner : вывести значения поля owner всех элементов в порядке возрастания"));
        };
        new Thread(help).start();
    }



    public String save(){
        return "";
    }

    /**
     * Показывает объекты в коллекции со всеми полями
     */
    public void show(ExecutorService poolSend, SelectionKey key) {
        Runnable show = () -> {
            Product product;
            String s = "";
            if (this.collection != null && !this.collection.isEmpty()) {
                for (Iterator var2 = this.collection.iterator(); var2.hasNext(); s = s + product.toString() + "\n") {
                    product = (Product) var2.next();
                }
            } else {
                s = "В коллекции нет элементов";
            }
            poolSend.submit(new ServerSender(key, s));
        };
        new Thread(show).start();
    }

    /**
     * Показывает информацию о коллекции: тип, дата создания, размер
     */
    public void info(ExecutorService poolSend, SelectionKey key) {
        Runnable info = () -> {
            poolSend.submit(new ServerSender(key, "Тип коллекции: " + this.collection.getClass().toString() + " дата создания:" + this.creationDate + " размер: " + this.collection.size()));
        };
        new Thread(info).start();
    }


    /**
     * Добавляет новый элемент в коллекцию
     * @param product: объект класса Product
     */
    public synchronized void add(Product product, ExecutorService poolSend, SelectionKey key, BDActivity bdActivity, String s) {
        Runnable add = () -> {
            try {
                bdActivity.addToSQL(product, s, product.getId());
                this.collection.add(product);
                poolSend.submit(new ServerSender(key, "Объект успешно добавлен в коллекцию"));
            }catch (SQLException e){
                poolSend.submit(new ServerSender(key, "Объект не добавлен, вероятно проблема с SQL"));
            }
        };
        new Thread(add).start();
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
     *
     */
    public synchronized void update(Product product, ExecutorService poolSend, SelectionKey key, BDActivity bdActivity, String s) {
        Runnable update = () ->{
            try {
                int flag = bdActivity.update(product.getId(), s, product);
                collection.clear();
                collection = bdActivity.loadFromSQL("properties");
                if (flag > 0 ){
                poolSend.submit(new ServerSender(key, "элемент успешно обновлен,"));
                }else {
                    poolSend.submit(new ServerSender(key, "элемент c этим id принадлежит другому пользователю или его не существует"));
                }
            } catch (SQLException e){
                e.printStackTrace();
             poolSend.submit(new ServerSender(key, "Элемента с таким id нету в коллекции, или вы не имеете прав на его изменение"));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        new Thread(update).start();
    }

    public void updateInPrograms(Product product, int id) {
        boolean flag = false;
        String s = "";
        Iterator var5 = this.collection.iterator();

        while (var5.hasNext()) {
            Product product1 = (Product) var5.next();
            if (product1.getId() == id) {
                this.collection.remove(product1);
                this.collection.add(product);
                s = "Элемент с id = " + id + " успешно обновлён";
                flag = true;
                break;
            }
        }
    }

    public synchronized void removeByID(String id, ExecutorService poolSend, SelectionKey key, BDActivity bdActivity, String s) {
        Runnable update = () ->{
            try {
                int flag = bdActivity.deleteById(Integer.parseInt(id), s);
                collection.clear();
                collection = bdActivity.loadFromSQL("properties");
                if (flag > 0){
                poolSend.submit(new ServerSender(key, "элемент успешно удален"));
                }else {
                    poolSend.submit(new ServerSender(key, "элемент принадлежит другому пользователю или его не существует"));
                }
            } catch (SQLException e){
                e.printStackTrace();
                poolSend.submit(new ServerSender(key, "Элемента с таким id нету в коллекции, или вы не имеете прав на его изменение"));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        new Thread(update).start();
    }

    /**
     * Метод для удаления объекта с заданным id
     * @param argument - id объекта, который надо удалить
     */
    public String removeByIDInPrograms(String argument) {
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

    public synchronized void clear(SelectionKey key, ExecutorService poolSend,  BDActivity bdActivity, String s){
        Runnable clear = () -> {
            try {
                bdActivity.clearSQL(s);
                collection.clear();
                collection = bdActivity.loadFromSQL("properties");
                poolSend.submit(new ServerSender(key, "Все элементы принадлежавшие вам удаленны из коллекции"));
            }catch (SQLException e){
                e.printStackTrace();
                poolSend.submit(new ServerSender(key, "Произошла ошибка SQL, возможно в коллекции нету элементов, которые принадлежат вам"));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        new Thread(clear).start();
    }

    /**
     * Удаляет все объекты из коллекции
     */
    public String clearInProgram(String login) {
        return "";
    }

    /**
     * Метод для добавления объекта, если он больше наибольшего объекта в коллекции
     * @param product - объект для добавления
     */
    public synchronized void addIfMax(Product product, SelectionKey key, ExecutorService poolSend,  BDActivity bdActivity, String login) {
        Runnable addIfMax =() -> {
            try {

                if (product.compareTo(collection.first()) > 0) {
                    try {
                        bdActivity.addToSQL(product, login, product.getId());
                        collection.add(product);
                        poolSend.submit(new ServerSender(key, "элемент добавлен'"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        poolSend.submit(new ServerSender(key, "элемент не подходит под условия"));
                    }
                } else {
                    poolSend.submit(new ServerSender(key, "коллекция обновлена"));
                }
            }catch (NoSuchElementException e){
                poolSend.submit(new ServerSender(key, "в коллекции нет элементов для сравнения"));
            }
        };
        new Thread(addIfMax).start();
    }

    /**
     * Метод для добавления объекта, если он меньше наименьшего объекта в коллекции
     * @param product - объект для добавления
     */
    public synchronized void addIfMin(Product product, SelectionKey key, ExecutorService poolSend,  BDActivity bdActivity, String login) {
        Runnable addIfMin =() -> {
            try {
                if (product.compareTo(collection.last()) < 0) {
                    try {
                        bdActivity.addToSQL(product, login, product.getId());
                        collection.add(product);
                        poolSend.submit(new ServerSender(key, "элемент добавлен'"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        poolSend.submit(new ServerSender(key, "элемент не подходит под условия"));
                    }
                } else {
                    poolSend.submit(new ServerSender(key, "коллекция обновлена"));
                }
            }catch (NoSuchElementException e){
                poolSend.submit(new ServerSender(key, "в коллекции нет элементов для сравнения"));
            }
        };
        new Thread(addIfMin).start();
    }

    /**
     * Метод для удаления объектов, если они больше заданного объекта
     * @param product - объект для сравнения
     */
    public synchronized void removeGreater(Product product, SelectionKey key, ExecutorService poolSend, BDActivity bdActivity, String s) {
        Runnable removeGreater = () ->{
            try {
                int flag =  bdActivity.deleteByY(product.getCoordinates().getY(), s);
                collection.clear();
                collection = bdActivity.loadFromSQL("properties");
                if (flag > 0){
                poolSend.submit(new ServerSender(key, "элементы удалены"));
                }else {
                    poolSend.submit(new ServerSender(key, "нету элементов подходящих под условие"));
                }
            } catch (SQLException e) {
                poolSend.submit(new ServerSender(key, "Нету элементов для удаления, которые удовлетворяют условиям"));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        new Thread(removeGreater).start();
    }

    /**
     * Метод для вывода количество элементов, значение поля owner которых меньше заданного
     * @param product - объект для сравнения
     */
    public synchronized void countLessThanOwner(Product product, SelectionKey key, ExecutorService poolSend) {
        Runnable countLessThanOwner = () -> {
            int counter = 0;
            Iterator var3 = this.collection.iterator();

            while (var3.hasNext()) {
                Product product1 = (Product) var3.next();
                if (product1.getOwner().compareTo(product.getOwner()) < 0) {
                    ++counter;
                }
            }
            poolSend.submit(new ServerSender(key, "Количество элементов меньше заданного: " + counter));
        };
        new Thread(countLessThanOwner).start();

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
    public synchronized void printFieldAscendingOwner(SelectionKey key, ExecutorService poolSend) {
        Runnable printFieldAscendingOwner = () -> {
            String s = "";
            if (!this.collection.isEmpty()) {
                Product product1;
                for (Iterator var2 = this.collection.iterator(); var2.hasNext(); s = product1.getOwner().toString() + "\n" + s) {
                    product1 = (Product) var2.next();
                }

                System.out.println(s);
            } else {
                s = "В коллекции нет элементов";
            }
            poolSend.submit(new ServerSender(key, s));
        };
        new Thread(printFieldAscendingOwner).start();
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

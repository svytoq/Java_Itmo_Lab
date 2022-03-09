package busineslogic.service;

import model.Person;
import model.Product;
import java.io.File;
import java.util.Date;
import java.util.TreeSet;

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
    public CollectionManager(TreeSet<Product> collection){
        this.collection = collection;
        creationDate = new Date();
    }

    /**
     * Показывает объекты в коллекции со всеми полями
     */
    public void show(){
        if (collection == null || collection.isEmpty()){
            System.out.println("В коллекции нет элементов");
        }
        for (Product product : collection){
            System.out.println(product.toString());
        }
    }

    /**
     * Показывает информацию о коллекции: тип, дата создания, размер
     */
    public void info(){
        System.out.println("Тип коллекции: " + collection.getClass().toString() + " дата создания:" + creationDate + " размер: " + collection.size());

    }

    /**
     * Добавляет новый элемент в коллекцию
     * @param product: объект класса Product
     */
    public void add(Product product){
        collection.add(product);
        System.out.println("Объект успешно добавлен");
    }

    /**
     * Метод получения нового Id
     * @return Возвращает новый уникальный Id
     */
    public int getNewID() {
        if (collection == null || collection.isEmpty()) {
            return 1;
        } else {
            int i = 1;
            for (Product product: collection){
                i = Math.max(i, product.getId());
            }
            return i + 1;
        }
    }

    /**
     * Метод проверки нахождение объекта с заданным Id в коллекции
     * @param id Id
     * @return true если объект с таким id есть, иначе false
     */
    public boolean checkId(int id){
        boolean flag = false;
        for (Product product : collection){
            if (id == product.getId()){
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
    public void update(Product product, int id){
        boolean flag = false;
        for (Product product1 : collection){
            if (product1.getId() == id){
                collection.remove(product1);
                collection.add(product);
                System.out.println("Элемент с id = " + id +" успешно обновлён");
                flag = true;
                break;
            }
        }
        if (!flag){
        System.out.println("Элемента с таким id нету в коллекции");
        }
    }

    /**
     * Метод для удаления объекта с заданным id
     * @param id - id объекта, который надо удалить
     */
    public void removeByID( int id){
        boolean flag = false;
        for (Product product1 : collection){
            if (product1.getId() == id){
                collection.remove(product1);
                System.out.println("Элемент с id = " + id +" успешно удален");
                flag = true;
                break;
            }
        }
        if (!flag){
            System.out.println("элемента с таким id не существует");
        }
    }

    /**
     * Удаляет все объекты из коллекции
     */
    public void clear(){
        collection.clear();
        System.out.println("элементы коллекции успешно удалены");
    }

    /**
     * Метод для добавления объекта, если он больше наибольшего объекта в коллекции
     * @param product - объект для добавления
     */
    public void addIfMax(Product product){
        if (product.compareTo(collection.first()) > 0){
            collection.add(product);
            System.out.println("Элемент успешно добавлен в коллекцию");
        }else {
            System.out.println("Элемент меньше чем наибольший элемент в коллекции");
        }
    }

    /**
     * Метод для добавления объекта, если он меньше наименьшего объекта в коллекции
     * @param product - объект для добавления
     */
    public void addIfMin(Product product){
        if (product.compareTo(collection.last()) < 0){
            collection.add(product);
            System.out.println("Элемент успешно добавлен в коллекцию");
        }else {
            System.out.println("Элемент больше чем наименьший элемент в коллекции");
        }

    }

    /**
     * Метод для удаления объектов, если они больше заданного объекта
     * @param product - объект для сравнения
     */
    public void removeGreater(Product product){
        collection.removeIf(product1 -> product1.compareTo(product) > 0);
        System.out.println("Элементы удалены.");
    }

    /**
     * Метод для вывода количество элементов, значение поля owner которых меньше заданного
     * @param person - объект для сравнения
     */
    public void countLessThanOwner(Person person){
        int counter = 0;
        for (Product product1 : collection){
            if (product1.getOwner().compareTo(person) < 0  ){
                counter++;
            }
        }
        System.out.println("Количество элементов меньше заданного: " + counter);
    }

    /**
     * Метод для вывода элементов в порядке убывания
     * так как используемая коллекция TreeSet, которая хранит объекты в натуральном порядке,
     * то ничем не отличается от команды show
     */
    public void printDescending(){
        if (!(collection == null || collection.isEmpty())){
        for (Product product1 :collection){
            System.out.println(product1.toString());
        }
        }else {
            System.out.println("В коллекции нет элементов");
        }
    }

    /**
     * Метод для вывода поля owner в порядке возрастания
     */
    public void printFieldAscendingOwner(){
        String s = "";
        if (!collection.isEmpty()){
        for (Product product1 :collection){
            s = product1.getOwner().toString() + "\n" + s;
        }
            System.out.println(s);
        }else {
            System.out.println("В коллекции нет элементов");
        }
    }

    /**
     * Метод для загрузки коллекции
     * @param collection1 - коллекция загруженная из файла
     */
    public void load(TreeSet<Product> collection1) {
        if (! (collection1 == null)) {
            collection.addAll(collection1);
        }
    }

    /**
     * Метод для возвращения коллекции
     * @return collection - коллекция
     */
    public TreeSet<Product> getCollection() {
        return collection;
    }

}

package busineslogic.service;

import java.io.File;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Product;
import java.io.*;
import java.lang.reflect.Type;
import java.util.TreeSet;

/**
 * Класс отвечающий за работу с файлами
 * @autor Svytoq
 * @version 1.0
 */
public class FileManager {

    /** Поле путя к файлу */
    String nameFile;

    /**
     * Конструктор - создание нового объекта
     */
    public FileManager() {
    }

    /**
     * Метод для загрузки данных из файла в коллекцию
     * @param nameFile - путь до файла
     * @return коллекцию с объектами полученными из файла
     */
    public TreeSet<Product> load(String nameFile) {
        this.nameFile = nameFile;
        TreeSet<Product> fromXML = new TreeSet<>();
        try {
            File file = new File(nameFile);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String result = "";
            String line;
            try {
                while ((line = fileReader.readLine()) != null) {
                    result = result + line;
                }
            }catch (IOException e){
                System.out.println("файл пуст?");
            }

            TypeReference<TreeSet<Product>> typeProduct = new TypeReference<TreeSet<Product>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            };
            try {
                fromXML = parse().readValue(result, typeProduct);
                System.out.println("Коллекция успешно загруженна");
            }catch ( JsonProcessingException e ){
                System.out.println("данные в файле невозможно преобразовать к нашей коллекции");
            }
            try {
                fileReader.close();
            } catch (IOException e){
                System.out.println("поток уже закрыт?");
            }
        }catch (FileNotFoundException e) {
            System.out.println("файла не существует или к нему отсутсвует доступ");
        }
        return fromXML;
    }

    /**
     * вспомогательный метод для получения объекта XmlMapper
     * @return возвращает объект класса XmlMapper
     */
    public XmlMapper parse(){
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        return mapper;
    }

    /**
     * Метод для сохранения коллекции из текущей программы в файл
     * @param collection - коллекция в текущей программе
     */
    public void save(TreeSet<Product> collection)  {
        File file = new File(nameFile);
        if (!file.exists()){
            System.out.println("файл указанный для сохранения/загрузки не существует");
        }else if (!file.canRead() || !file.canWrite()) {
            System.out.println("Файл не доступен для чтения или записи");
        }else if (file.isDirectory()){
            System.out.println("Это директория, укажите файл");
        }else {
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
                fileWriter.write(parse().writeValueAsString(collection));
                fileWriter.close();
                System.out.println("коллекция успешно сохранена");
            }catch ( IOException e){
                System.out.println("поток закрыт?");
            }
        }
    }

}

package Manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.TreeSet;
import model.Product;

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
        TreeSet fromXML = new TreeSet();

        try {
            File file = new File(nameFile);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String result = "";

            String line;
            try {
                while((line = fileReader.readLine()) != null) {
                    result = result + line;
                }
            } catch (IOException var11) {
                System.out.println("файл пуст?");
            }

            TypeReference typeProduct = new TypeReference<TreeSet<Product>>() {
                public Type getType() {
                    return super.getType();
                }
            };

            try {
                fromXML = (TreeSet)this.parse().readValue(result, typeProduct);
                System.out.println("Коллекция успешно загруженна");
            } catch (JsonProcessingException var10) {
                System.out.println("данные в файле невозможно преобразовать к нашей коллекции");
            }

            try {
                fileReader.close();
            } catch (IOException var9) {
                System.out.println("поток уже закрыт?");
            }
        } catch (FileNotFoundException var12) {
            System.out.println("файла не существует или к нему отсутсвует доступ");
        }

        return fromXML;
    }

    /**
     * вспомогательный метод для получения объекта XmlMapper
     * @return возвращает объект класса XmlMapper
     */
    public XmlMapper parse() {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        return mapper;
    }

    /**
     * Метод для сохранения коллекции из текущей программы в файл
     * @param collection - коллекция в текущей программе
     */
    public String save(TreeSet<Product> collection) {
        String s = "";
        File file = new File(this.nameFile);
        if (!file.exists()) {
            s = "файл указанный для сохранения/загрузки не существует";
        } else if (file.canRead() && file.canWrite()) {
            if (file.isDirectory()) {
                s = "Это директория, укажите файл";
            } else {
                try {
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
                    fileWriter.write(this.parse().writeValueAsString(collection));
                    fileWriter.close();
                    s = "коллекция успешно сохранена";
                } catch (IOException var5) {
                    System.out.println("поток закрыт?");
                }
            }
        } else {
            s = "Файл не доступен для чтения или записи";
        }

        return s;
    }
}


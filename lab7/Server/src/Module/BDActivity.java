package Module;
import model.*;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.TreeSet;

public class BDActivity {
    private ResultSet res;
    private Connection connect;
    private PreparedStatement ps;
    private Statement statement;
    private Product product;
    private MessageDigest hash;

    /**
     * Метод подключает сервер к БД и загружает данные из таблицы
     *
     * @param file
     * @return
     * @throws ClassNotFoundException
     */
    public TreeSet<Product> loadFromSQL(String file) throws ClassNotFoundException, IOException, SQLException, NullPointerException {
        TreeSet<Product> col = new TreeSet<>();
        FileInputStream bd = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(bd);
        String url = properties.getProperty("BD.location");
        String login = properties.getProperty("BD.login");
        String password = properties.getProperty("BD.password");
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver successfully connected");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
        }
        connect = DriverManager.getConnection(url, login, password);
        statement = connect.createStatement();
        res = statement.executeQuery("SELECT * FROM product;");
        String name;
        Coordinates coordinates;
        LocalDateTime creationDate;
        Double price;
        String partNumber;
        double manufactureCost;
        UnitOfMeasure unitOfMeasure;
        Person owner;
        while (res.next()) {
            int id = res.getInt("id");
            name = res.getString("name");
            double x = res.getDouble("x");
            long y = res.getLong("y");
            coordinates = new Coordinates(x,y);
            creationDate = LocalDateTime.now();
            price = res.getDouble("price");
            partNumber = res.getString("partNumber");
            manufactureCost = res.getDouble("manufactureCost");
            try {
                unitOfMeasure = UnitOfMeasure.valueOf(res.getString("unitOfMeasure"));
            } catch (IllegalArgumentException e) {
                unitOfMeasure = null;
            }
            String nameOfPerson = res.getString("nameOfPerson");
            float weight = res.getFloat("weight");
            Color color;
            try {
                color = Color.valueOf(res.getString("eyecolor"));
            } catch (IllegalArgumentException e) {
                color = null;
            }
            int locationX = res.getInt("locationX");
            float locationY = res.getFloat("locationY");
            long locationZ = res.getLong("locationZ");
            String locationName = res.getString("locationName");
            Location location = new Location(locationX, locationY, locationZ, locationName);
            Person person = new Person(nameOfPerson, weight, color, location);
            String loginP = res.getString("loginP");

            Product product = new Product(id, name, coordinates, creationDate, price, partNumber, manufactureCost, unitOfMeasure, person, loginP);
            col.add(product);
        }
        System.out.println("Сервер подключился к БД");
        return col;
    }

    /**
     * Метод регестрирует пользователя в БД
     *
     * @param commandDescription
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String registration(CommandDescription commandDescription) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            hash = MessageDigest.getInstance("SHA-384");
            PreparedStatement ps = connect.prepareStatement("INSERT INTO product_login_password (login, password) VALUES (?, ?);");
            ps.setString(1, commandDescription.getLogin());
            ps.setString(2, Base64.getEncoder().encodeToString(hash.digest(commandDescription.getPassword().getBytes("UTF-8"))));
            ps.execute();
            System.out.println("Пользователь с логином " + commandDescription.getLogin() + " успешно зарегестрирован");
            return "Регистрация прошла успешно";
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при работе с БД (вероятно что-то с БД)");
            return "Ошибка с бд регистрация не удалась (скорее всего такой пользователь уже существует)";
        }
    }

    /**
     * Метод авторизует пользователя
     *
     * @param commandDescription
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean authorization(CommandDescription commandDescription) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            hash = MessageDigest.getInstance("SHA-384");
            ps = connect.prepareStatement("SELECT * FROM product_login_password WHERE (login = ?);");
            ps.setString(1, commandDescription.getLogin());
            res = ps.executeQuery();
            res.next();
            return Base64.getEncoder().encodeToString(hash.digest(commandDescription.getPassword().getBytes("UTF-8")))
                    .equals(res.getString("password"));
        } catch (SQLException e) {
         //   logger.error("Ошибка при работе с БД (вероятно что-то с БД)");
            return false;
        }
    }

    /**
     * Метод добавляет элемент в БД
     *
     * @param product
     * @param login
     * @throws SQLException
     */
    public void addToSQL(Product product, String login, long id) throws SQLException, NullPointerException {
        ps = connect.prepareStatement("INSERT INTO product (id, name, x, y, " +
                "creationDate, price, partNumber, manufactureCost, unitOfMeasure, nameOfPerson, weight, eyeColor, locationX, locationY, locationZ,locationName, loginP) " +
                "VALUES (? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setLong(1, id);
        ps.setString(2, product.getName());
        ps.setDouble(3, product.getCoordinates().getX());
        ps.setLong(4, product.getCoordinates().getY());
        ps.setObject(5, product.getCreationDate());
        try {
            ps.setDouble(6, product.getPrice());
        }catch (NullPointerException e){
            ps.setObject(6, null);
        }
        try {
            ps.setObject(7, String.valueOf(product.getPartNumber()));
        }catch (NullPointerException e){
            ps.setObject(7, null);
        }
        ps.setDouble(8, product.getManufactureCost());
        ps.setObject(9, String.valueOf(product.getUnitOfMeasure()));
        ps.setString(10, product.getOwner().getName());
        ps.setFloat(11, product.getOwner().getWeight());
        ps.setObject(12, String.valueOf(product.getOwner().getEyeColor()));
        ps.setInt(13, product.getOwner().getLocation().getX());
        ps.setFloat(14, product.getOwner().getLocation().getY());
        ps.setLong(15, product.getOwner().getLocation().getX());
        try {
            ps.setString(16, product.getOwner().getLocation().getName());
        }catch (NullPointerException e){
            ps.setString(16, null);
        }
        ps.setString(17, login);
        ps.execute();
    }

    /**
     * Метод получает сгенерированное id
     *
     * @return
     * @throws SQLException
     */
    public int getSQLId() throws SQLException {
        ResultSet res = statement.executeQuery("SELECT nextval('idSGsequence');");
        res.next();
        return res.getInt(1);
    }

    /**
     * Метод удаляет все элементы из SQL принадлежавшие одному пользователю
     *
     * @param login
     * @throws SQLException
     */
    public void clearSQL(String login) throws SQLException {
        ps = connect.prepareStatement("DELETE FROM product WHERE loginP = ?;");
        ps.setString(1, login);
        ps.execute();
    }

    /**
     * Удаляет все элементы из БД если они превышают указанный y
     *
     * @param login
     * @throws SQLException
     */
    public int deleteByY(long y, String login) throws SQLException {
        ps = connect.prepareStatement("DELETE FROM product WHERE(y > ?) AND (loginP = ?)");
        ps.setLong(1, y);
        ps.setString(2, login);
       return ps.executeUpdate();
    }

    /**
     * Удаляет элемент из БД по его id
     *
     * @param id
     * @param login
     * @throws SQLException
     */
    public int deleteById(int id, String login) throws SQLException {
        ps = connect.prepareStatement("DELETE FROM product WHERE(id = ?) AND (loginP = ?)");
        ps.setInt(1, id);
        ps.setString(2, login);
        return ps.executeUpdate();
    }

    /**
     * Удаляет элемент из БД по его students count
     *
     * @param studentsCount
     * @param login
     * @throws SQLException
     */
    public void deleteByStudentsCount(int studentsCount, String login) throws SQLException {
        ps = connect.prepareStatement("DELETE FROM product WHERE(studentsCount = ?) AND (loginP = ?)");
        ps.setInt(1, studentsCount);
        ps.setString(2, login);
        ps.execute();
    }

    /**
     * Метод обновляет в БД элемент по его id
     *
     * @param id
     * @param login
     * @throws SQLException
     */
    public int update(int id, String login, Product product) throws SQLException, NullPointerException {
        ps = connect.prepareStatement("UPDATE product SET name = ? , x = ? , y = ?" +
                ", price = ?, partNumber = ? , manufactureCost = ?, unitOfMeasure = ?, nameOfPerson = ?, weight = ?, eyeColor = ? " +
                ", locationX = ?, locationY = ?, locationZ = ?, locationName = ? WHERE id = ? AND loginP = ?;");
        ps.setString(1, product.getName());
        ps.setDouble(2, product.getCoordinates().getX());
        ps.setLong(3, product.getCoordinates().getY());
        try {
            ps.setDouble(4, product.getPrice());
        }catch (NullPointerException e){
            ps.setObject(4, null);
        }
        try {
            ps.setObject(5, String.valueOf(product.getPartNumber()));
        }catch (NullPointerException e){
            ps.setObject(5, null);
        }
        ps.setDouble(6, product.getManufactureCost());
        ps.setObject(7, String.valueOf(product.getUnitOfMeasure()));
        ps.setString(8, product.getOwner().getName());
        ps.setFloat(9, product.getOwner().getWeight());
        ps.setObject(10, String.valueOf(product.getOwner().getEyeColor()));
        ps.setInt(11, product.getOwner().getLocation().getX());
        ps.setFloat(12, product.getOwner().getLocation().getY());
        ps.setLong(13, product.getOwner().getLocation().getZ());
        try {
            ps.setString(14, product.getOwner().getLocation().getName());
        }catch (NullPointerException e){
            ps.setString(14, null);
        }
        ps.setInt(15, id);
        ps.setString(16, login);
        return ps.executeUpdate();
    }
}

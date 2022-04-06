package Module;


/**
 * Входная точка в программу
 * @autor Svytoq
 * @version 1.0
 */
public class ClientSide {
    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.connection();
    }
}

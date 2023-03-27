import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<ClientHandler> clientHandlers = new HashSet<>();

    public Server(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server port: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                ClientHandler client = new ClientHandler(socket, this);
                clientHandlers.add(client);
                client.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void sendToAllUsers(String message, ClientHandler excludeUser) {
        for (ClientHandler aUser : clientHandlers) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    void addUserName(String userName) {
        userNames.add(userName);
    }

    void removeUser(String userName, ClientHandler client) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            clientHandlers.remove(client);
            System.out.println(userName + " logged out");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    public static void main(String[] args) {
        int port = 8000;
        Server server = new Server(port);
        server.execute();
    }
}
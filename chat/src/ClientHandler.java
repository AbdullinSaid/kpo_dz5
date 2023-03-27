import java.net.*;
import java.io.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            writer.println("Connected users: " + server.getUserNames());

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.sendToAllUsers(serverMessage, this);

            String clientMessage = "";
            while (!clientMessage.equals("end session")) {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.sendToAllUsers(serverMessage, this);
            }

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " logged out";
            server.sendToAllUsers(serverMessage, this);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}
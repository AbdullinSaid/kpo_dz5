import java.io.*;
import java.net.*;
import java.util.Scanner;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Client client;

    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter username:");
        String userName = in.nextLine();
        client.setUserName(userName);
        writer.println(userName);

        String message = "";
        while (!message.equals("end session")) {
            message = in.nextLine();
            writer.println(message);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}

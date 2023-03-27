import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Client client;

    public ReadThread(Socket socket, Client client) {
        this.client = client;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println(response);

                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
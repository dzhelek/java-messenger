import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Room {
    int portNumber;
    String name;
    ServerSocket server;
    List<Socket> clients = new ArrayList<>();

    static int startPortNumber = 1213;

    public Room(String name) {
        this.name = name;
        portNumber = startPortNumber++;

        try {
            server = new ServerSocket(portNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void join() throws IOException {
        while (true) {
            Socket socket = server.accept();
            Thread thread = new Thread(() -> handler(socket));
            clients.add(socket);
            thread.start();
        }
    }

    @Override
    public String toString() {
        return "[" + portNumber + "] " + name;
    }

    void handler(Socket socket) {
        String message;
        try(
            Scanner receiver = new Scanner(socket.getInputStream());
            PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
        )
        {
            while (true) {
                if (receiver.hasNext()) {
                    message = receiver.nextLine();
                    sender.println(message);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

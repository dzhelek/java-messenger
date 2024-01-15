import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static List<Room> chatRooms = new ArrayList<>();
    private static final int defaultPort = 1212;
    public static void main(String[] args) throws IOException {
        try(
            ServerSocket serverSocket = new ServerSocket(defaultPort);
        ){
            while (true) {
                Socket socket = serverSocket.accept();
//                new Handler(socket).start();
                new Thread(() -> handler(socket)).start();
            }
        }
    }

    static void handler(Socket socket) {
        try (
            PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
            Scanner receiver = new Scanner(socket.getInputStream());
        ) {
            sender.println("Welcome: (1) - create new room; (2) - join existing room");
            String mode = receiver.nextLine();
            switch (mode) {
                case "1" -> createRoom(sender, receiver);
                case "2" -> joinRoom(sender, receiver);
                default -> {
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void joinRoom(PrintWriter sender, Scanner receiver) throws IOException {
        sender.println(chatRooms);
        String portNumber = receiver.nextLine();
        sender.println("RECONNECT " + portNumber);
//        String port = receiver.nextLine();
//        int portInt = Integer.parseInt(port);
//        for(Room room : chatRooms) {
//            if (room.portNumber == portInt) {
//                room.join();
//                break;
//            }
//        }
    }

    private static void createRoom(PrintWriter sender, Scanner receiver) throws IOException {
        sender.println("Enter room name: ");
        String name = receiver.nextLine();
        Room room = new Room(name);
        chatRooms.add(room);
        sender.println("RECONNECT " + room.portNumber);
        room.join();
    }
}

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String message;
        String command = "";

        int port;
        try (
            Socket socket = new Socket("localhost", 1212);
            Scanner userInput = new Scanner(System.in);
            Scanner receiver = new Scanner(socket.getInputStream());
            PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
        ) {
            while (true) {
                command = receiver.nextLine();
                if (command.contains("RECONNECT")) {
                    break;
                }
                System.out.println(command);
                message = userInput.nextLine();
                sender.println(message);
            }

            port = Integer.parseInt(command.split(" ")[1]);
        }
        try(
            Socket socket = new Socket("localhost", port);
            Scanner receiver = new Scanner(socket.getInputStream());
        ){
            System.out.println("joined room: " + port);
            new Thread(() -> readUserInput(socket)).start();
            while (true) {
                System.out.println(receiver.nextLine());
            }
        }
    }

    static void readUserInput(Socket socket) {
        String message;
        try(
            Scanner userInput = new Scanner(System.in);
            PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
        ) {
            while(true) {
                if (userInput.hasNext()) {
                    message = userInput.nextLine();
                    sender.println(message);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
        
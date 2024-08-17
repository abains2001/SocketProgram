package socketprogram;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TextClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n[[[[[[[[Main Menu]]]]]]]]");
                System.out.println("0. Connect to the server");
                System.out.println("1. Get the user list");
                System.out.println("2. Send a message");
                System.out.println("3. Get my messages");
                System.out.println("4. Exit");
                System.out.print("Enter a menu option via number 0-4: ");
                int option = scanner.nextInt();
                scanner.nextLine(); 
                System.out.println();
                switch (option) {
                    case 0 -> {
                        System.out.print("Enter Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine();
                        out.println("CONNECT:" + username + "," + password);
                        System.out.println(in.readLine());
                    }
                    case 1 -> {
                        out.println("GET_USER_LIST:");
                        System.out.println("User List: " + in.readLine());
                    }
                    case 2 -> {
                        System.out.print("Enter recipient username: ");
                        String recipient = scanner.nextLine();
                        System.out.print("Enter message: ");
                        String message = scanner.nextLine();
                        out.println("SEND_MESSAGE:" + recipient + "," + message);
                        System.out.println(in.readLine());
                    }
                    case 3 -> {
                        out.println("GET_MESSAGES:");
                        System.out.println("Messages: " + in.readLine());
                    }
                    case 4 -> {
                        out.println("EXIT:");
                        System.out.println(in.readLine());
                        return; 
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


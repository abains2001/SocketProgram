package socketprogram;

import java.io.*;
import java.net.*;
import java.util.*;

public class TextServer {
    private static final int PORT = 12345;
    private static Map<String, String> userCredentials = new HashMap<>();
    private static Map<String, List<String>> messages = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started");

        userCredentials.put("Alice", "1234");
        userCredentials.put("Bob", "5678");

        messages.put("Alice", new ArrayList<>());
        messages.put("Bob", new ArrayList<>());

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String input = in.readLine();
                    if (input == null) break;
                    String[] parts = input.split(":", 2);
                    String command = parts[0];

                    switch (command) {
                        case "CONNECT":
                            connectCommand(parts[1]);
                            break;
                        case "GET_USER_LIST":
                            getUserListCommand();
                            break;
                        case "SEND_MESSAGE":
                            sendMessageCommand(parts[1]);
                            break;
                        case "GET_MESSAGES":
                            getMessageCommand();
                            break;
                        case "EXIT":
                            exitCommand();
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void connectCommand(String credentials) {
            String[] parts = credentials.split(",");
            String user = parts[0];
            String pass = parts[1];
            if (userCredentials.containsKey(user) && userCredentials.get(user).equals(pass)) {
                username = user;
                out.println("Access Granted");
            } else {
                out.println("Username/Password Incorrect");
            }
        }

        private void getUserListCommand() {
            out.println(String.join(",", userCredentials.keySet()));
        }

        private void sendMessageCommand(String messageData) {
            String[] parts = messageData.split(",", 2);
            String recipient = parts[0];
            String message = parts[1];
            if (messages.containsKey(recipient)) {
                messages.get(recipient).add("From " + username + ": " + message);
                out.println("Message sent to " + recipient);
            } else {
                out.println("User not found");
            }
        }

        private void getMessageCommand() {
            List<String> userMessages = messages.get(username);
            if (userMessages.isEmpty()) {
                out.println("<<You have no messages>>");
            } else {
                out.println(String.join("\n", userMessages));
                userMessages.clear(); 
            }
        }

        private void exitCommand() {
            out.println("<<<<Client Exiting. Good bye.>>>>");
        }
    }
}

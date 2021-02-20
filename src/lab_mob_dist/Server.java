package lab_mob_dist;

import java.net.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

public class Server {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started at 1234 port");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } finally {
            if (serverSocket != null)
                try {
                    serverSocket.close();
                    System.out.println("Disconnected from 1234 port");
                } catch (IOException e) {
                }
        }
    }
}

class Connection extends Thread{

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private static HashMap<String, ArrayList<String>> chatRooms = new HashMap<>();

    public Connection(Socket client) {
        try {
            socket = client;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            while(true){
                String data = in.readUTF();
                System.out.println("Received: " + data);

                String params[] = data.split(":");

                switch (params[0]){
                    case "create":
                        chatRooms.put(params[1], new ArrayList<>());
                        break;
                    case "list":
                        if (params[1].equals("members")) {
                            out.writeUTF(String.valueOf(chatRooms.get(params[2])));
                        } else {
                            out.writeUTF(String.valueOf(chatRooms.keySet()));
                        }
                        break;
                    case "find":
                        out.writeBoolean(chatRooms.containsKey(params[1]));
                        break;
                    case "join":
                        chatRooms.get(params[2]).add(params[1]);
                        break;
                    case "exit":
                        chatRooms.get(params[2]).removeIf(member -> member.equals(params[1]));
                        break;
                }
            }

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Closing the connection.");
        }

    }
}
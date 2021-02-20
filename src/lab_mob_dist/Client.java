package lab_mob_dist;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int PORT = 1234;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Socket socket = new Socket("localhost", PORT);

    public Client() throws IOException {
    }

    public void createChatRoom(String groupIp) throws IOException {
        out = new DataOutputStream(socket.getOutputStream());
        try {
            InetAddress group = InetAddress.getByName(groupIp);
            if(group.isMulticastAddress()){
                out.writeUTF("create:" + groupIp);
                System.out.println("Group " + groupIp + " has been created");
                return;
            }
            System.out.println(groupIp + " isn't a valid Multicast IP");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void listChatRooms() throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        try {
            out.writeUTF("list:chatrooms");
            String data = in.readUTF();
            System.out.println("* ------------------------ *");
            System.out.println("Chat rooms created: ");
            System.out.println("IP: " + data);
            System.out.println("* ------------------------ *");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinChatRoom(String groupIp) throws IOException {
        Scanner scan = new Scanner(System.in);
        ChatRoom chatRoom = new ChatRoom();
        try {
            out = new DataOutputStream(socket.getOutputStream());
            if(hasChatRoom(groupIp)){
                System.out.println("Inform you name: ");
                String name = scan.nextLine();
                out.writeUTF("join:" + name + ":" + groupIp);
                System.out.println("Group: " + groupIp);

                chatRoom.joinChatRoom(name, groupIp);
                chatRoom.showMessages();
                do {
                    String msg = scan.nextLine();
                    if (msg.equals("exit group")) {
                        chatRoom.leaveChatRoom();
                        out.writeUTF("exit:" + name + ":" + groupIp);
                        System.out.println("Leaving chat room " + groupIp);
                        break;
                    } else if (msg.equals("list members")) {
                        out.writeUTF("list:members:" + groupIp);
                        String data = in.readUTF();
                        System.out.println(data);
                    } else {
                        chatRoom.sendMessage(name + ": " + msg);
                    }

                } while(chatRoom.isChatting());
            }else {
                System.out.println("Create a chat, with a valid multicast IP, before join it");
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        }
    }

    private Boolean hasChatRoom(String groupIp) throws IOException {
        out.writeUTF("find:" + groupIp);
        return in.readBoolean();
    }

    public void listMembers(String groupIp){}

    public void shutDown() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
        System.exit(0);
    }

    public static void main(String args[]) throws IOException {
        Client client = new Client();
        while(true){
            Scanner scan = new Scanner(System.in);
            System.out.println("Select the desired option: 1 - Create a chat room | 2 - Join a chat room" +
                    " | 3 - List chat rooms | 4 - Shut down");
            Integer menu = scan.nextInt();

            switch(menu){
                case 1:
                    System.out.println("Inform the Multicast ip of the chat room you want to create:");
                    client.createChatRoom(scan.next());
                    break;
                case 2:
                    System.out.println("Inform the group ip you want to join:");
                    String groupIp = scan.next();
                    client.joinChatRoom(groupIp);
                    break;
                case 3:
                    client.listChatRooms();
                    break;
                case 4:
                    client.shutDown();
                    break;
            }
        }
    }
}
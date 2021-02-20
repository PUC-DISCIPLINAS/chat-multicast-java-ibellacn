package lab_mob_dist;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

class ChatRoom {
    private static final int PORT = 1234;
    private MulticastSocket mSocket = new MulticastSocket(PORT);;
    private InetAddress groupIp;
    private boolean isChatting = true;
    private String username = "";

    ChatRoom() throws IOException {
    }

    public void joinChatRoom(String name, String address) {
        String msg = " " + name + " joined the chat room";
        this.username = name;
        try {
            this.groupIp = InetAddress.getByName(address);
            this.mSocket.joinGroup(groupIp);
            this.sendMessage(msg);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        try {
            byte[] message = msg.getBytes();
            DatagramPacket messageOut = new DatagramPacket(message, message.length, this.groupIp, PORT);
            this.mSocket.send(messageOut);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public void showMessages() {
        Thread thread = new Thread(() -> {
            try {
                while (this.isChatting) {
                    byte[] buffer = new byte[1000];
                    DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length, this.groupIp, PORT);
                    this.mSocket.receive(messageIn);
                    System.out.println( new String(messageIn.getData()).trim());
                }
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        });
        thread.start();
    }

    public void leaveChatRoom() {
        try {
            mSocket.leaveGroup(this.groupIp);
            this.isChatting = false;
            String msgExitGroup = " " + this.username + " leave this chat room";
            this.sendMessage(msgExitGroup);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public boolean isChatting() { return isChatting; }

    public void setChatting(boolean chatting) { isChatting = chatting; }
}


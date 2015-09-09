package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Created by Dario on 2015-09-08.
 */
public class ClientThread extends Thread{
    private DatagramSocket socket = null;
    private InetAddress hostname;
    private int port = 6478;
    private boolean connected;

    public ClientThread()throws IOException{
        connected = false;
        socket = new DatagramSocket();
        hostname = InetAddress.getByName("127.0.0.1");
    }
    public void run(){
        System.out.println("Client is ready!");
        connected = true;
        String msgOut;
        Scanner scanner = new Scanner(System.in);
        while(connected){

            System.out.print("> ");
            while((msgOut = scanner.nextLine()) != null){
                sendMessage(msgOut);
                if(msgOut.equalsIgnoreCase("bye")){
                    closeConnection();
                    return;
                }
                String msg = getMessage();
                displayMessage(msg);
                System.out.print("> ");
            }

        }
        scanner.close();
    }

    private void sendMessage(String msgOut){
        byte[] bufferOut;
        DatagramPacket packetOut;
        try{
            //Send message to server
            bufferOut = msgOut.getBytes();
            packetOut = new DatagramPacket(bufferOut, bufferOut.length, hostname, port);
            socket.send(packetOut);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void displayMessage(String msg) {
        if(msg.equalsIgnoreCase("ok")){
            System.out.println("Connected!");
        }
        System.out.println(msg);
    }

    private String getMessage(){
        try{
            byte[] bufferIn = new byte[256];
            DatagramPacket packetIn = new DatagramPacket(bufferIn, bufferIn.length);
            socket.receive(packetIn);
            return new String(packetIn.getData(), 0, packetIn.getLength());
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void closeConnection(){
        connected = false;
        socket.close();
    }

}

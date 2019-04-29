package com.muc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
    public static void main(String[] args) {
        int port = 8818;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                System.out.println("About to accept Client Connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                OutputStream outputStream = clientSocket.getOutputStream();
                for(int i=0; i < 10; i++) {
                    outputStream.write(("Loading... \n Time now is " + new Date() + "\n").getBytes());
                    Thread.sleep(1000);
                }
                outputStream.write("Hello World\n".getBytes());
                clientSocket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

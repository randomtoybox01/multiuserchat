package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Serverworker extends Thread {

    private final Socket clientSocket;

    public Serverworker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientSocket(Socket clientSocket) throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ( (line = reader.readLine()) != null) {
            if ("quit".equalsIgnoreCase(line)) {
                break;
            }
            String msg = "You typed: " + line + "\n";
            outputStream.write(msg.getBytes());
        }

        clientSocket.close();
    }
}

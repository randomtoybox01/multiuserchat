package com.muc;

import org.apache.commons.lang3.StringUtils;
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
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(line)) {
                    break;
                }else if("login".equalsIgnoreCase(cmd)){
                    handlelogin(outputStream, tokens);
                } else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
                String msg = "You typed: " + line + "\n";
                outputStream.write(msg.getBytes());
            }
        }

        clientSocket.close();
    }

    private static void handlelogin(OutputStream outputStream, String[] tokens) {
    }
}

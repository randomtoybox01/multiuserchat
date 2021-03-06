package com.muc;
/**
 * @author Cole McGuire
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that runs the server and initializes Sockets.
 */
public class Server extends Thread {
    private final int serverPort;

    private ArrayList<ServerWorker> workerlist = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkerList() {
        return workerlist;
    }

    @Override
    /**
     * Starts the Server and ServerWorker
     */
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                System.out.println("About to accept Client Connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                ServerWorker worker = new ServerWorker( this, clientSocket);
                workerlist.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes the ServerWorker
     * @param serverWorker
     */
    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
    }
}

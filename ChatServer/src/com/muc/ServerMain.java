package com.muc;
/**
 * @author Cole McGuire
 */
/**
 * Initializes server port and starts the server.
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = 8818;
        Server server = new Server(port);
        server.start();
    }
}

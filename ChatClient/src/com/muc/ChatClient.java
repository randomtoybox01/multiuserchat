package com.muc;
/**
 * @author Cole McGuire
 */
import org.apache.commons.lang3.StringUtils;

import javax.print.DocFlavor;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ChatClient: A class that handles the users, login/logoffs, and messages
 */
public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();

    /**
     * Constructor that sets up class parameters
     * @param serverName
     * @param serverPort
     */
    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    /**
     * Starts up a handler for the user's chat,
     * handler controls the sending and receiving of messages.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 8818);
        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
            }
        });
        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("You got a message from " + fromLogin + "====>" + msgBody);
            }
        });

        if (!client.connect()) {
            System.err.println("Connection Failed");
        } else {
            System.out.println("Connection successful");
            if(client.login("guest", "guest")) {
                System.out.println("login Successful");
            } else {
                System.err.println("Login failed");
            }

        }
    }

    /**
     * Boolean method that handle's the login and
     * returns a true if the user logged on successfully and a false otherwise.
     * @param login
     * @param password
     * @return
     * @throws IOException
     */
    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response Line: " + response);

        if("ok login".equalsIgnoreCase(response)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }

    }

    /**
     * Initializes a thread to begin a chat.
     */
    private void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    /**
     * checks tokens and reads the message inputted.
     */
    private void readMessageLoop() {
        try {
        String line;
        while((line = bufferedIn.readLine()) != null) {
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        handleOffline(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)){
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokensMsg);
                    }
                }
            }
        } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    /**
     * checks the tokens of the message in order
     * to determine the sender and receiver.
     * @param tokensMsg
     */
    private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for (MessageListener listener : messageListeners) {
            listener.onMessage(login, msgBody);
        }
    }

    /**
     * checks the tokens to determine if a user is offline.
     * @param tokens
     */
    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    /**
     * checks the tokens to determine if a user is online.
     * @param tokens
     */
    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.online(login);
        }
    }

    /**
     * connects to user to the server using sockets.
     * @return
     */
    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Client port is " + socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * the following 4 methods all either add or remove
     * a UserStatusListener or a MessageListener
     * @param listener
     */
    public void addUserStatusListener(UserStatusListener listener) {
        userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    /**
     * handles the structure of a message so the
     * message adheres to the protocol.
     * @param sendTo
     * @param msgBody
     * @throws IOException
     */
    public void msg(String sendTo, String msgBody) throws IOException {
            String cmd = "msg " + sendTo + " " + msgBody + "\n";
            serverOut.write(cmd.getBytes());
    }
}

package com.muc;
/**
 * @author Cole McGuire
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Class that sets up a JPane for messaging other users
 */
public class MessagePane extends JPanel implements MessageListener {

    private final String login;
    private final ChatClient client;

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private JTextField inputField = new JTextField();

    /**
     * Constructor that initializes JPane and it's parameters
     * @param client
     * @param login
     */
    public MessagePane(ChatClient client, String login) {
        this.client = client;
        this.login = login;

        client.addMessageListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(messageList), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = inputField.getText();
                    client.msg(login, text);
                    listModel.addElement(text);
                    inputField.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Sends to message and alerts the user
     * @param fromLogin
     * @param msgBody
     */
    public void onMessage(String fromLogin, String msgBody) {
        String line = fromLogin + ": " + msgBody;
        listModel.addElement(line);
    }
}

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
 * Class that initiates the Login JPane
 */
public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");

    public LoginWindow() {
        super("Login");

        this.client = new ChatClient("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginField);
        p.add(passwordField);
        p.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER);

        pack();

        setVisible(true);
    }

    /**
     * handles the login and confirms the users identity
     */
    private void doLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();
        try {
            if (client.login(login, password)) {
                UserListPane userListPane = new UserListPane(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);

                frame.getContentPane().add(new JScrollPane(userListPane), BorderLayout.CENTER);
                frame.setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login/password,");
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * runs the class
     * @param args
     */
    public static void main(String[] args) {
        LoginWindow loginwin = new LoginWindow();

    }
}

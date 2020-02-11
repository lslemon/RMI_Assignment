package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Log_in
{
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JLabel welcomeLabel = new JLabel("Welcome to NUIG Assessments");
    private JLabel loginLabel = new JLabel("Please Log in");
    private JLabel studentIDLabel = new JLabel("Student ID");
    private JLabel passwordLabel = new JLabel("Password");
    private JPasswordField passwordField;
    private JTextField studentIdField;
    private PanelListener listener;

    public interface PanelListener
    {
        public void onStudentIdEntered(String text);
        public void onPasswordEntered(String text);
    }

    public Log_in()
    {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(welcomeLabel);
        rootPanel.add(loginLabel);

        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));

        passwordField = new JPasswordField();
        studentIdField = new JTextField();

        loginPanel.add(studentIDLabel);
        loginPanel.add(studentIdField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        rootPanel.add(loginPanel);

    }

    public void setPanelListener(PanelListener listener)
    {
        this.listener = listener;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}

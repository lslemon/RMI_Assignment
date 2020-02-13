package client;

import assessment.ExamServer;
import engine.ExamEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

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
    private JButton logInButton;
    private PanelListener listener;
    private ExamServer examServer;
    private int counter;

    private String password;
    private Integer studentId;

    public interface PanelListener
    {
        public void onStudentIdEntered(String text);
        public void onPasswordEntered(String text);
        public void onLoginInfoEntered(String password, Integer studentId);
    }

    public Log_in(ExamServer examServer)
    {
        this.examServer = examServer;

        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(welcomeLabel);
        rootPanel.add(Box.createRigidArea(new Dimension(60, 60)));
        rootPanel.add(loginLabel);

        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));

        passwordField = new JPasswordField();
        passwordField.setSize(200, 50);
        passwordField.addActionListener(actionListener);
        studentIdField = new JTextField();
        studentIdField.setSize(200, 50);
        studentIdField.addActionListener(actionListener);

        loginPanel.add(studentIDLabel);
        loginPanel.add(studentIdField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        logInButton = new JButton("Log In");
        logInButton.addActionListener(actionListener);
        loginPanel.add(logInButton);
        rootPanel.add(Box.createRigidArea(new Dimension(60, 60)));
        rootPanel.add(loginPanel);
    }

    public void setPanelListener(PanelListener listener)
    {
        this.listener = listener;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e)
        {

        }

        @Override
        public void focusLost(FocusEvent e)
        {
            if(e.getSource() == passwordField)
            {
                System.out.println("Received password field event");
//                listener.onPasswordEntered(e.getActionCommand());
                password = new String(((JPasswordField)e.getComponent()).getPassword());
            }

            if(e.getSource() == studentIdField)
            {
                System.out.println("Received text field event");
//                listener.onStudentIdEntered(e.getActionCommand());
                String str = ((JTextField)e.getComponent()).getText();
                //TODO Handle Inappropriate data entries
                try
                {
                    studentId = new Integer(str);
                }
                catch (NumberFormatException exception)
                {
                    rootPanel.add(new JLabel("Student ID must be numerical"));
                }
            }
        }
    };

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == passwordField)
            {
                System.out.println("Received password field event");
//                listener.onPasswordEntered(e.getActionCommand());
                password = e.getActionCommand();
            }

            if(e.getSource() == studentIdField)
            {
                System.out.println("Received text field event");
//                listener.onStudentIdEntered(e.getActionCommand());
                String str = e.getActionCommand();
                //TODO Handle Inappropriate data entries
                studentId = new Integer(e.getActionCommand());
            }

            if(e.getSource() == logInButton)
            {
                System.out.println("Pressed");
                if(studentId == null || password == null)
                {
                    rootPanel.add(new JLabel("Missing username or password"));
                    return;
                }

                if(!(studentId instanceof Integer))
                {
                    rootPanel.add(new JLabel("Student ID must be numerical"));
                    return;
                }

                listener.onLoginInfoEntered(password, studentId);

            }
        }
    };
}

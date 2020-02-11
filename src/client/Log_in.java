package client;

import assessment.ExamServer;
import engine.ExamEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private PanelListener listener;
    private ExamServer examServer;
    private int counter;

    private String password;
    private int studentId;

    public interface PanelListener
    {
        public void onStudentIdEntered(String text);
        public void onPasswordEntered(String text);
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
        passwordField.addActionListener(textChangeListener);
        studentIdField = new JTextField();
        studentIdField.setSize(200, 50);
        studentIdField.addActionListener(textChangeListener);

        loginPanel.add(studentIDLabel);
        loginPanel.add(studentIdField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

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

    private ActionListener textChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == passwordField)
            {
                counter++;
                password = e.getActionCommand();
            }

            if(e.getSource() == studentIdField)
            {
                counter++;
                studentId = new Integer(e.getActionCommand());
            }

            if(counter == 2)
            {
                try {
                     System.out.println(examServer.login(studentId, password));
                } catch (UnauthorizedAccess unauthorizedAccess) {
                    unauthorizedAccess.printStackTrace();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };
}

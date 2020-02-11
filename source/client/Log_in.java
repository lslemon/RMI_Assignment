package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Log_in
{
    private JPanel rootPanel;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private PanelListener listener;

    public interface PanelListener
    {
        public void onStudentIdEntered(String text);
        public void onPasswordEntered(String text);
    }

    public Log_in()
    {

    }

    public void setPanelListener(PanelListener listener)
    {
        this.listener = listener;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}

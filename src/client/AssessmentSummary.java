package client;

import assessment.ExamServer;
import assessment.NoMatchingAssessment;
import engine.ExamEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AssessmentSummary
{
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JLabel welcomeLabel;
    private JLabel notificationLabel;

    private List<String> assessmentSummary = new LinkedList<String>();

    private PanelListener listener;
    private ExamServer examServer;
    private int counter;

    private int studentId;
    private int token;

    public interface PanelListener
    {
        public void onStudentIdEntered(String text);
        public void onPasswordEntered(String text);
    }

    public AssessmentSummary(ExamServer examServer, int token, int studentId)
    {
        this.token = token;
        this.studentId = studentId;
        this.examServer = examServer;

        try {
            assessmentSummary = examServer.getAvailableSummary(token, studentId);
        } catch (UnauthorizedAccess unauthorizedAccess) {
            unauthorizedAccess.printStackTrace();
        } catch (NoMatchingAssessment noMatchingAssessment) {
            noMatchingAssessment.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        welcomeLabel = new JLabel("Welcome Student " + studentId);
        notificationLabel = new JLabel("You have "+assessmentSummary.size()+" Assessments waiting for completion");

        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(welcomeLabel);
        rootPanel.add(notificationLabel);
        rootPanel.add(Box.createRigidArea(new Dimension(60, 60)));


    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    };
}

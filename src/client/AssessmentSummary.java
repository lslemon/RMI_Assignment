package client;

import assessment.Assessment;
import assessment.ExamServer;
import assessment.NoMatchingAssessment;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class AssessmentSummary
{
    private JPanel rootPanel;
    private JLabel welcomeLabel;
    private JLabel notificationLabel;
    private JList jListTodo;
    private JList jListCompleted;

    private List<String> assessmentSummary = new LinkedList<>();
    private List<String> assessmentsCompleted = new LinkedList<>();

    private SummaryListener listener;
    private ExamServer examServer;
    private int counter;

    private int studentId;
    private int token;


    public interface SummaryListener
    {
        public void onAssessmentChosen(Assessment assessment);
    }

    //TODO Handle double click in JList
    public AssessmentSummary(ExamServer examServer, int token, int studentId)
    {
        this.token = token;
        this.studentId = studentId;
        this.examServer = examServer;

        loadAssessments();

    }

    public void loadAssessments()
    {
        try {
            assessmentSummary = examServer.getAvailableSummary(token, studentId);
        } catch (RemoteException | UnauthorizedAccess | NoMatchingAssessment e) {
            e.printStackTrace();
        }

        for(String string : assessmentSummary)
        {
            if(string.contains("COMPLETED"))
            {
                assessmentSummary.remove(string);
                assessmentsCompleted.add(string);
            }
        }

        welcomeLabel = new JLabel("Welcome Student " + studentId);
        notificationLabel = new JLabel("You have "+assessmentSummary.size()+" Assessments waiting for completion");

        jListTodo = new JList(assessmentSummary.toArray());
        jListTodo.setLayoutOrientation(JList.VERTICAL);
        jListTodo.addListSelectionListener(listSelectionListener);

        jListCompleted = new JList(assessmentsCompleted.toArray());
        jListCompleted.setLayoutOrientation(JList.VERTICAL);
        jListCompleted.addListSelectionListener(listSelectionListener);

        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(welcomeLabel);
        rootPanel.add(notificationLabel);
        rootPanel.add(Box.createRigidArea(new Dimension(60, 60)));

        rootPanel.add(jListTodo);
    }

    public void setListener(SummaryListener listener) {
        this.listener = listener;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private ListSelectionListener listSelectionListener = new ListSelectionListener() {
        int counter = 0;
        @Override
        public void valueChanged(ListSelectionEvent e)
        {
//            counter ++;
//            if(counter == 2)
//            {
                counter = 0;
                String assessmentInfo = (String) jListTodo.getSelectedValue();
                System.out.println(assessmentInfo);
                String courseCode = assessmentInfo.substring(assessmentInfo.length()-3);
                Assessment assessment = null;
                try {
                    assessment = examServer.getAssessment(token, studentId, courseCode);
                } catch (UnauthorizedAccess unauthorizedAccess) {
                    unauthorizedAccess.printStackTrace();
                } catch (NoMatchingAssessment | RemoteException ex) {
                    ex.printStackTrace();
                }
                listener.onAssessmentChosen(assessment);
                System.out.println(assessment.getQuestions().size());
//            }
        }
    };

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    };
}
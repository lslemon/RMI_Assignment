package client;

import assessment.Assessment;
import assessment.ExamServer;
import assessment.NoMatchingAssessment;
import assessment.Question;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class AssessmentQuestions
{
    private JPanel rootPanel;
    private JList jListQuestions;

    private PanelListener listener;
    private ExamServer examServer;
    private int counter;
    private Assessment assessment;

    private int studentId;
    private int token;

    public interface PanelListener
    {
        public void onStudentIdEntered(String text);
        public void onPasswordEntered(String text);
    }

    //TODO Handle double click in JList
    public AssessmentQuestions(ExamServer examServer, int token, int studentId, Assessment assessment)
    {
        this.assessment = assessment;
        this.token = token;
        this.studentId = studentId;
        this.examServer = examServer;

        List<JPanel> questionPanels = new LinkedList<>();

        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(Box.createRigidArea(new Dimension(60, 60)));


        for(Question question: assessment.getQuestions())
        {
            System.out.println(question.getQuestionDetail());
            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.PAGE_AXIS));
            JLabel questionLabel = new JLabel(question.getQuestionDetail());
            questionPanel.add(questionLabel);
            for(String answer: question.getAnswerOptions())
            {
                System.out.println(answer);
                JRadioButton answerButton = new JRadioButton(answer);
                questionPanel.add(answerButton);
            }
            questionPanels.add(questionPanel);
            rootPanel.add(questionPanel);
        }

        jListQuestions = new JList(questionPanels.toArray());
        jListQuestions.add(new JLabel(assessment.getQuestions().get(0).getQuestionDetail()));
        jListQuestions.add(new JLabel(assessment.getQuestions().get(1).getQuestionDetail()));
        jListQuestions.setLayoutOrientation(JList.VERTICAL);
        jListQuestions.addListSelectionListener(listSelectionListener);
        
//        rootPanel.add(jListQuestions);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private ListSelectionListener listSelectionListener = new ListSelectionListener() {
        int counter = 0;
        @Override
        public void valueChanged(ListSelectionEvent e)
        {
            counter ++;
            if(counter == 2)
            {
                counter = 0;
                String assessmentInfo = (String) jListQuestions.getSelectedValue();
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

                System.out.println(assessment.getQuestions().size());
            }
        }
    };

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    };
}
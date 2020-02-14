package client;

import assessment.Assessment;
import assessment.ExamServer;
import assessment.Question;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AssessmentQuestions
{
    private JPanel rootPanel;
    private JButton submitButton;
    private JButton returnButton;

    private QuestionListener listener;
    private Assessment assessment;

    public interface QuestionListener
    {
        public void onAssignmentSubmission(Assessment assessment);
        public void onReturnPressed();
    }

    //TODO Handle double click in JList
    public AssessmentQuestions(Assessment assessment)
    {
        this.assessment = assessment;

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
                answerButton.addActionListener(answerActionListener);
                questionPanel.add(answerButton);
            }
            rootPanel.add(questionPanel);
        }

        rootPanel.add(Box.createRigidArea(new Dimension(100,100)));

        submitButton = new JButton("Submit");
        submitButton.addActionListener(actionListener);

        returnButton = new JButton("Return");
        returnButton.addActionListener(actionListener);

        rootPanel.add(submitButton);
        rootPanel.add(returnButton);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setQuestionListener(QuestionListener listener) {
        this.listener = listener;
    }

    private ActionListener answerActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
        }
    };

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == submitButton)
            {
                listener.onAssignmentSubmission(assessment);
            }

            if(e.getSource() == returnButton)
            {
                listener.onReturnPressed();
            }
        }
    };
}
package client;

//Luke Slemon 16421694

import assessment.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.lang.Object;

public class AssessmentQuestions
{
    private JPanel rootPanel;
    private JButton submitButton;
    private JButton returnButton;

    private QuestionListener listener;
    private Assessment assessment;
    private List<Question> questions;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.M.yyyy hh:mm");

    private List<ButtonGroup> questionButtons;

    public interface QuestionListener
    {
        public void onAssignmentSubmission(Assessment assessment);
        public void onReturnPressed();
    }

    //TODO Handle double click in JList
    public AssessmentQuestions(Assessment assessment)
    {
        this.assessment = assessment;
        Date dueDate = assessment.getClosingDate();
        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(Box.createRigidArea(new Dimension(60,150)));
        rootPanel.add(new JLabel(assessment.getInformation()));
        rootPanel.add(new JLabel("Assessment Due: "+dateFormat.format(dueDate)));
        rootPanel.add(Box.createRigidArea(new Dimension(60,60)));
        questions = new LinkedList<>();
        questionButtons = new LinkedList<>();

        for(Question question: assessment.getQuestions())
        {
            questions.add(question);
            ButtonGroup answerOptions = new ButtonGroup();
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
                answerOptions.add(answerButton);
            }

            rootPanel.add(questionPanel);
            questionButtons.add(answerOptions);
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

    public void loadPreviousAnswers()
    {
        for(Question question: assessment.getQuestions())
        {
            int asns = assessment.getSelectedAnswer(question.getQuestionNumber());
        }
    }

    public void setQuestionListener(QuestionListener listener) {
        this.listener = listener;
    }

    private ActionListener answerActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            for(ButtonGroup buttonGroup: questionButtons)
            {
                if(((JRadioButton)e.getSource()).getModel().getGroup().equals(buttonGroup))
                {
                    int optionNum = 0;
                    int questionNum = questionButtons.indexOf(buttonGroup);
                    for(String answerOption: questions.get(questionNum).getAnswerOptions())
                    {
                        if(answerOption.equals(e.getActionCommand()))
                        {
                            try {
                                assessment.selectAnswer(questionNum, optionNum);
                            } catch (InvalidQuestionNumber | InvalidOptionNumber e1) {
                                e1.printStackTrace();
                            }
                        }
                        optionNum++;
                    }
                }
            }
            System.out.println(((JRadioButton)e.getSource()).getText());
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
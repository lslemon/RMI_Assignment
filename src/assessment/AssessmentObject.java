// AssessmentObject.java 
package assessment;

import java.util.*;

public class AssessmentObject implements Assessment {
    
    private Date close_date;
    private String info;
    private int student_id;
    private String course_code;
    private int mcq_question_num;
    private List<Question> questions = new ArrayList<Question>();
    private List<Integer> answers = new LinkedList<Integer>();

    public AssessmentObject(String information, Date closing_date, int id)
    {
        this.info = information;
        this.close_date = closing_date;
        this.student_id = id;

        questions.add(new QuestionObj(1, "Who is cooler", new String[]{"SAM", "LUKE"}));
        questions.add(new QuestionObj(2, "Why", new String[]{"HAIR", "SMARTS"}));
    }

    // Return information about the assessment	
    public String getInformation()
    {
        return this.info;
    }

	// Return the final date / time for submission of completed assessment
    public Date getClosingDate()
    {
        return this.close_date;
    }

	// Return a list of all questions and anser options
    public List<Question> getQuestions()
    {
        return this.questions;
    }

	// Return one question only with answer options
	public Question getQuestion(int questionNumber) throws InvalidQuestionNumber
    {   
        if(questions.size()-1 > questionNumber)
        {
            throw new InvalidQuestionNumber();
        }
        return questions.get(questionNumber);
    }

	// Answer a particular question
    public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber
    {
        if(questionNumber <= questions.size()-1)
        {
            if(optionNumber <= mcq_question_num - 1)
            {
                answers.add(questionNumber,optionNumber);
            }
            else
            {
                throw new InvalidOptionNumber();
            }
        }
        else
        {
            throw new InvalidQuestionNumber();
        }

    }

	// Return selected answer or zero if none selected yet
    public int getSelectedAnswer(int questionNumber)
    {
        return answers.get(questionNumber);
    }

	// Return studentid associated with this assessment object
	// This will be preset on the server before object is downloaded
    public int getAssociatedID()
    {
        return this.student_id;
    }

}
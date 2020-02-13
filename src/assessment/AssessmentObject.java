package assessment;
// AssessmentObject.java 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssessmentObject implements Assessment {
    
    private Date close_date;
    private String info;
    private int student_id;
    private int course_id;
    private int mcq_question_num;
    private List<Question> questions = new ArrayList<Question>();
    private List<Integer> answers = new ArrayList<Integer>();

    public AssessmentObject(String information, Date closing_date, int id, int course_code)
    {
        this.info = information;
        this.close_date = closing_date;
        this.student_id = id;
        this.course_id = course_code;
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
        if(questions.size()-1 > questionNumber || questionNumber <= 0)
        {
            throw new InvalidQuestionNumber("Invalid Question Number");
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
            	throw new InvalidOptionNumber("Invalid Option Number");
            }
        }
        else
        {
        	 throw new InvalidQuestionNumber("Invalid Question Number");
        }

    }

	// Return selected answer or zero if none selected yet
    public int getSelectedAnswer(int questionNumber)
    {
        return answers.get(questionNumber);
    }

	// Return student id associated with this assessment object
	// This will be preset on the server before object is download
    public int getAssociatedID()
    {
        return this.student_id;
    }

}
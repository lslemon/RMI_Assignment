package engine;
// Question.java

import assessment.Question;

public class QuestionObj implements Question
{	
	private static final long serialVersionUID = 1L;
	private int number;
	private String details;
	private String[] options = new String[200];

	public QuestionObj(int num, String question, String[] opts)
	{
		this.number = num;
		this.details = question;
		this.options = opts;
	}

	// Return the question number
	public int getQuestionNumber()
	{
		return this.number;
	}

	// Return the question text
	public String getQuestionDetail()
	{
		return this.details;
	}

	// Return the possible answers to select from
	public String[] getAnswerOptions()
	{
		return this.options;
	}

}

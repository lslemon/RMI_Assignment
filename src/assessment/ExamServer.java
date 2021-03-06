// ExamServer.java

package assessment;

import client.UnauthorizedAccess;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ExamServer extends Remote {

	// Return an access token that allows access to the server for some time period
	public int login(int studentid, String password) throws
			RemoteException, UnauthorizedAccess;

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid) throws
		UnauthorizedAccess, RemoteException, NoMatchingAssessment;

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode) throws
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed) throws
			UnauthorizedAccess, NoMatchingAssessment, RemoteException, InvalidOptionNumber, InvalidQuestionNumber;

}


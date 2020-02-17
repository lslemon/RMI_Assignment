package client;

//Luke Slemon 16421694
import assessment.*;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client
{
    private static int studentId;
    private static int counter = 0;

    private Client()
    {

    }

    private void startClient()
    {
        try
        {
            System.out.print("....Booting up");
            System.out.println("Instantiating Exam Engine");

            //Attact to registry at Port 20345
            Registry registry = LocateRegistry.getRegistry(20345);

            //Get stub code from registry associated with "ExamServer"
            ExamServer engine = (ExamServer) registry.lookup("ExamServer");

            //Create Log in Panel class
            Log_in login = new Log_in();
            JFrame frame = new JFrame("NUIG Assessments");
            frame.setContentPane(login.getRootPanel());
            frame.setVisible(true);
            frame.setSize(700, 600);

            //Login Panel Listener used for handling any Panel events
            login.setPanelListener(new Log_in.LogInListener() {
                @Override
                public void onLoginInfoEntered(String password, Integer studentId)
                {
                    System.out.println("Log on");
                    int token = 0;
                    try
                    {
                        //Log into the Exam Server using entered id and password
                        token = engine.login(studentId, password);
                    } catch (UnauthorizedAccess | RemoteException e)
                    {
                        e.printStackTrace();
                        return;
                    }

                    //Use token and ID to get a summary of assessments
                    AssessmentSummary assessmentSummary = new AssessmentSummary(engine, token, studentId);

                    //Swap Login panel with summary panel
                    frame.setContentPane(assessmentSummary.getRootPanel());
                    frame.setVisible(true);
                    int finalToken = token;
                    assessmentSummary.setListener(new AssessmentSummary.SummaryListener() {
                        @Override
                        /**
                         * When an assessment is chosen the course code,
                         * the assessment questions panel is opened
                         */
                        public void onAssessmentChosen(Assessment assessment) {
                            AssessmentQuestions assessmentQuestions =
                                    new AssessmentQuestions(assessment);
                            frame.setContentPane(assessmentQuestions.getRootPanel());
                            frame.setVisible(true);
                            assessmentQuestions.setQuestionListener(new AssessmentQuestions.QuestionListener() {
                                /**
                                 * Submission listener handles when a user wishes to submit an assignment
                                 * @param assessment
                                 */
                                @Override
                                public void onAssignmentSubmission(Assessment assessment)
                                {
                                    try {
                                        engine.submitAssessment(finalToken, studentId, assessment);
                                    } catch (UnauthorizedAccess | NoMatchingAssessment | RemoteException
                                            | InvalidOptionNumber | InvalidQuestionNumber e) {
                                        e.printStackTrace();
                                    }
                                    //Reload the assessment summary as the current panel
                                    assessmentSummary.loadAssessments();
                                    frame.setContentPane(assessmentSummary.getRootPanel());
                                    frame.setVisible(true);
                                }

                                /**
                                 * Handles when return button pressed.
                                 * Reloads the summary panel without submitting any assignments
                                 */
                                @Override
                                public void onReturnPressed()
                                {
                                    assessmentSummary.loadAssessments();
                                    frame.setContentPane(assessmentSummary.getRootPanel());
                                    frame.setVisible(true);
                                }
                            });
                        }

                        /**
                         * Close listener handles when user presses the close button
                         * and closes the application.
                         */
                        @Override
                        public void onClosePressed()
                        {
                            System.exit(0);
                        }
                    });
                }
            });

        } catch(Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client();
        client.startClient();
    }
}

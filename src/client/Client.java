package client;

import assessment.Assessment;
import assessment.ExamServer;
import assessment.NoMatchingAssessment;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client
{
    private static int studentId;
    private static String password;
    private static int counter = 0;

    private Client()
    {
        startClient();
    }

    private void startClient()
    {
        try
        {
            System.out.print("....Booting up");
            System.out.println("Instantiating Exam Engine");

            Scanner scanner = new Scanner(System.in);
            Registry registry = LocateRegistry.getRegistry(20345);

            ExamServer engine = (ExamServer) registry.lookup("ExamServer");

            Log_in login = new Log_in(null);
            JFrame frame = new JFrame("NUIG Assessments");
            frame.setContentPane(login.getRootPanel());
            frame.setVisible(true);
            frame.setSize(700, 600);
            login.setPanelListener(new Log_in.LogInListener() {
                @Override
                public void onStudentIdEntered(String text)
                {
                    counter++;
                    studentId = new Integer(text);
                    if(counter == 2)
                    {
                        counter =0;
                        System.out.println("Log in");
                        AssessmentSummary assessmentSummary = new AssessmentSummary(null, studentId, 1);
                        frame.setContentPane(assessmentSummary.getRootPanel());
                    }
                }

                @Override
                public void onPasswordEntered(String text)
                {
                    counter++;
                    password = text;
                    if(counter == 2)
                    {
                        counter =0;
                        System.out.println("Log in");
                    }
                }

                @Override
                public void onLoginInfoEntered(String password, Integer studentId)
                {
                    System.out.println("Log on");
                    int token = 0;
                    try
                    {
                        token = engine.login(studentId, password);
                    } catch (UnauthorizedAccess | RemoteException e)
                    {
                        e.printStackTrace();
                        return;
                    }

                    AssessmentSummary assessmentSummary = new AssessmentSummary(engine, token, studentId);
                    frame.setContentPane(assessmentSummary.getRootPanel());
                    frame.setVisible(true);
                    int finalToken = token;
                    assessmentSummary.setListener(new AssessmentSummary.SummaryListener() {

                        @Override
                        public void onAssessmentChosen(Assessment assessment) {
                            AssessmentQuestions assessmentQuestions =
                                    new AssessmentQuestions(assessment);
                            frame.setContentPane(assessmentQuestions.getRootPanel());
                            frame.setVisible(true);
                            assessmentQuestions.setQuestionListener(new AssessmentQuestions.QuestionListener() {
                                @Override
                                public void onAssignmentSubmission(Assessment assessment)
                                {
                                    try {
                                        engine.submitAssessment(finalToken, studentId, assessment);
                                    } catch (UnauthorizedAccess | NoMatchingAssessment | RemoteException e) {
                                        e.printStackTrace();
                                    }
                                    frame.setContentPane(assessmentSummary.getRootPanel());
                                    frame.setVisible(true);
                                }

                                @Override
                                public void onReturnPressed()
                                {
                                    frame.setContentPane(assessmentSummary.getRootPanel());
                                    frame.setVisible(true);
                                }
                            });
                        }
                    });
                }
            });

//            System.out.println("Enter Student ID");
//            studentId = 0;
//            try
//            {
//                studentId = scanner.nextInt();
//            }
//            catch (InputMismatchException e)
//            {
//                System.out.println("Student Id must be comprised of digits 0-9");
//                studentId = scanner.nextInt();
//            }
//            System.out.println("Enter your password");
//            password = scanner.nextLine();
//            int token = engine.login(studentId, password);
//
//            List<String> assessment_details = engine.getAvailableSummary(token, studentId);
//
//            for(String details : assessment_details)
//            {
//                System.out.println(details+"\n");
//            }
//
//            System.out.println("Choose an assessment using the correct course code");
//            String course_code = scanner.nextLine();
//
//            Assessment assessment = engine.getAssessment(token, studentId, course_code);
//
//            System.out.println(assessment.getInformation());
//            System.out.println(assessment.getClosingDate().toString());

        } catch(Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client();
    }
}

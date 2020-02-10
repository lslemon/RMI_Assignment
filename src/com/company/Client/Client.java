package com.company;

import com.company.ExamServer;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.LongFunction;

public class Client
{
    private Client(){}

    public static void main(String[] args)
    {
        int studentId;
        String password;

        try
        {
            System.out.print("....Booting up");
            System.out.println("Instantiating Exam Engine");

            Scanner scanner = new Scanner(System.in);
//            Registry registry = LocateRegistry.getRegistry(null);
//
//            ExamServer engine = (ExamServer) registry.lookup("ExamServer");
//
            Log_in login = new Log_in();
            JFrame frame = new JFrame("NUIG Assessments");
            frame.setContentPane(login.getRootPanel());
            frame.setVisible(true);
            frame.setSize(700, 1200);
            login.setPanelListener(new Log_in.PanelListener() {
                @Override
                public void onStudentIdEntered(String text)
                {
//                    studentId = new Integer(text);
                }

                @Override
                public void onPasswordEntered(String text)
                {
//                    password = text;
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
//            String password = scanner.nextLine();
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

//            System.out.println(assessment.getInformation());
//            System.out.println(assessment.getClosingDate().toString());

        } catch(Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

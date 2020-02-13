package engine;

import assessment.Assessment;
import assessment.AssessmentObject;
import assessment.ExamServer;
import assessment.NoMatchingAssessment;
import client.UnauthorizedAccess;

import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExamEngine implements ExamServer {

    private int max_session_time_min = 15;
    private Map<Integer, String> whitelist = new HashMap<Integer, String>();
    private Map<Integer, Date> tokens = new HashMap<Integer, Date>();
    private List<Assessment> assessments = new ArrayList<>();

    // Constructor is required
    public ExamEngine() 
    {
        super();
        whitelist.put(100,"password");
        whitelist.put(200,"password");
        assessments.add((Assessment)new AssessmentObject("Info", new Date(), 100, 123));
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws UnauthorizedAccess, RemoteException
    {
        System.out.println("Attempting Log in\nID: "+studentid+"\nPassword: "+password);
    	int token = 0;
        if(whitelist.containsKey(studentid) && whitelist.get(studentid).equals( password))
        {
            boolean unique = false;
            while(unique == false)
            {
                token = new Random().nextInt(9999);
                if(!tokens.containsKey(token))
                {
                    tokens.put(token, new Date());
                    return token;	
                }
            }
        }
        else
        {
            throw new UnauthorizedAccess("Incorrect Log in Details");
        }
        return 0;
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException
    {
        if(!checkToken(token))
        {
            throw new UnauthorizedAccess("Token has expired");
        }

        List<String> summaries = new ArrayList<String>();

        for(Assessment cur_assess: assessments)
        {
            if(cur_assess.getAssociatedID() == studentid)
            {
                summaries.add(cur_assess.getInformation());
            }
        }
        if(summaries.isEmpty())
        {
            throw new NoMatchingAssessment(String.format("No matching assessents for student id: %d", studentid));
        }
        return summaries;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) 
    throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        if(!checkToken(token))
            {
        		throw new UnauthorizedAccess("Token has expired");
            }

        for(Assessment cur_assess: assessments)
        {
            if(cur_assess.getAssociatedID() == studentid)
            {
                return cur_assess;
            }
        }
        throw new NoMatchingAssessment(String.format("No matching assessents for student id: %d", studentid));
    }

    /*Don't know why studentid is passed if you are giving me a completed Assessment object */
    public void submitAssessment(int token, int studentid, Assessment completed) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        if(!checkToken(token))
        {
        	throw new UnauthorizedAccess("Token has expired");
        }
        assessments.add(completed);
    }

    private boolean checkToken(int token) throws NoMatchingAssessment
    {   
        Date current_time = new Date();
        if(tokens.containsKey(token))
        {
            Date start_time = tokens.get(token);
            long session_time = current_time.getTime() - start_time.getTime();
            long session_time_sec = session_time / 1000 % 60;  
            if(session_time_sec < 60*max_session_time_min)
            {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
    	int registryport = 20345;

        if (args.length > 0)
           registryport = Integer.parseInt(args[0]);
        
        System.out.println("RMIRegistry port = " + registryport);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry(registryport);
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}

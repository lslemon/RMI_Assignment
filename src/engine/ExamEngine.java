
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
import java.util.*;


public class ExamEngine implements ExamServer {

    private int max_session_time_min = 15;
    private List<String> avail_summaries = new ArrayList<>();
    private Map<Integer, String> whitelist = new HashMap<Integer, String>();
    private Map<Integer, Date> tokens = new HashMap<Integer, Date>();
    private List<Assessment> assessments = new ArrayList<>();



    // Constructor is required
    public ExamEngine()
    {
        super();
        whitelist.put(100,"password");
        whitelist.put(200,"password");
        assessments.add(new AssessmentObject("First Assessment", new Date(), 100));
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws UnauthorizedAccess, RemoteException
    {
        int token = 0;
        if(whitelist.containsKey(100) && whitelist.get(100) == password)
        {
            boolean unique = false;
            while(unique == false)
            {
                token = new Random().nextInt(9999);
                if(!tokens.containsKey(token))
                {
                    tokens.put(token, new Date());
                }
            }
        }
        else
        {
            throw new UnauthorizedAccess("");
        }
        return token;
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws UnauthorizedAccess, NoMatchingAssessment, RemoteException
    {
        if(!checkToken(token))
        {
            throw new UnauthorizedAccess("Incorrect Token");
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
            throw new NoMatchingAssessment("No Assessments ready");
        }
        return summaries;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode)
            throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        if(!checkToken(token))
        {
            throw new UnauthorizedAccess("");
        }

        for(Assessment cur_assess: assessments)
        {
            if(cur_assess.getAssociatedID() == studentid && cur_assess.getInformation().contains(courseCode))
            {
                return cur_assess;
            }
        }
        throw new NoMatchingAssessment("");
    }

    /*Don't know why studentid is passed if you are giving me a completed Assessment object */
    public void submitAssessment(int token, int studentid, Assessment completed) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        if(!checkToken(token))
        {
            throw new UnauthorizedAccess("");
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
            if(session_time < 60*max_session_time_min)
            {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            ExamServer stub =
                    (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry(20345);
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}

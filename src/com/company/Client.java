package com.company;

import com.company.ExamServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client
{
    private Client(){}

    public static void main(String[] args)
    {
        try
        {
            Registry registry = LocateRegistry.getRegistry(null);

            ExamServer stub = (ExamServer) registry.lookup("ExamServer");
            List<String> strings = stub.getAvailableSummary(1,2);


        } catch(Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

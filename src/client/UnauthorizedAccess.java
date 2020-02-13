package client;

public class UnauthorizedAccess extends Exception 
{
	public UnauthorizedAccess(String reason) 
	{
		super(reason);
	}
}


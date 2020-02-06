package com.company;

public class UnauthorizedAccess extends Exception {

	public UnauthorizedAccess(String reason) {
		super(reason);
	}
}


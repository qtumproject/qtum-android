package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;


public class JSONRPCException extends Exception {

	public JSONRPCException(Object error)
	{
		super(error.toString());
	}
	
	public JSONRPCException(String message, Throwable innerException)
	{
		super(message, innerException);
	}
}

package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class JSONRPCClient {
	
	protected abstract JSONObject doJSONRequest(JSONObject request) throws JSONRPCException;
	
	protected static JSONArray getJSONArray(Object[] array){
		JSONArray arr = new JSONArray();
		for (Object item : array) {
			if(item.getClass().isArray()){
				arr.put(getJSONArray((Object[])item));
			}
			else {
				arr.put(item);
			}
		}
		return arr;
	}
	
	protected JSONObject doRequest(String method, Object[] params) throws JSONRPCException
	{
		JSONArray jsonParams = new JSONArray();
		for (int i=0; i<params.length; i++)
		{
			if(params[i].getClass().isArray()){
				jsonParams.put(getJSONArray((Object[])params[i]));
			} else{
				jsonParams.put(params[i]);
			}
		}

		JSONObject jsonRequest = new JSONObject();
		try 
		{
			jsonRequest.put("id", UUID.randomUUID().hashCode());
			jsonRequest.put("method", method);
			jsonRequest.put("params", jsonParams);
			jsonRequest.put("jsonrpc", "2.0");
		}
		catch (JSONException e1)
		{
			throw new JSONRPCException("Invalid JSON request", e1);
		}
		return doJSONRequest(jsonRequest);
	}
	
	protected JSONObject doRequest(String method, JSONObject params) throws JSONRPCException, JSONException {
		
		JSONObject jsonRequest = new JSONObject();
		try{
			jsonRequest.put("id", UUID.randomUUID().hashCode());
			jsonRequest.put("method", method);
			jsonRequest.put("params", params);
			jsonRequest.put("jsonrpc", "2.0");
		} catch (JSONException e1) {
			throw new JSONRPCException("Invalid JSON request", e1);
		}
		return doJSONRequest(jsonRequest);
	}

	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public Object call(String method, Object ... params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).get("result");
		} 
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result", e);
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public Object call(String method, JSONObject params) throws JSONRPCException {
		try{
			return doRequest(method, params).get("result");
		} catch (JSONException e) {
			throw new JSONRPCException("Cannot convert result to String", e);
		}
	}
	 
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a String
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public String callString(String method, Object ... params) throws JSONRPCException
	{
		try 
		{
			return doRequest(method, params).getString("result");
		} catch (JSONRPCException | JSONException e) {
			throw new JSONRPCException("Cannot convert result to String", e);
		}
	}


	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a String
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */	public String callString(String method, JSONObject params) throws JSONRPCException {
		try{
			return doRequest(method, params).getString("result");
		} catch (JSONException | JSONRPCException e) {
			throw new JSONRPCException("Cannot convert result to String", e);
		}
	}
	 
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as an int
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public int callInt(String method, Object ... params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);
			return response.getInt("result");
		} catch (JSONException e) {
			try{
				return Integer.parseInt(response.getString("result"));
			} catch(NumberFormatException | JSONException e1){
				throw new JSONRPCException("Cannot convert result to int", e1);
			}
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as an int
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */	
	public Object callInt(String method, JSONObject params) throws JSONRPCException {
		JSONObject response = null;
		try{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);
			return response.getInt("result");
		} catch (JSONException e) {
			try{
				return Integer.parseInt(response.getString("result"));
			} catch(NumberFormatException | JSONException e1){
				throw new JSONRPCException("Cannot convert result to int", e1);
			}
		}
	 }
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a long
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public long callLong(String method, Object ... params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);
			return response.getLong("result");
		} 
		catch (JSONException e)
		{
			try {
				return Long.parseLong(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to long", e);
			}
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a long
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public long callLong(String method, JSONObject params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);
			return response.getLong("result");
		} 
		catch (JSONException e)
		{
			try {
				return Long.parseLong(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to long", e);
			}
			
		}
	}	
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a boolean
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public boolean callBoolean(String method, Object ... params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getBoolean("result");
		} 
		catch (JSONException e)
		{
			try {
				return Boolean.parseBoolean(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to boolean", e1);
			}
			
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a boolean
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public boolean callBoolean(String method, JSONObject params) throws JSONRPCException {
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getBoolean("result");
		} 
		catch (JSONException e)
		{
			try {
				return Boolean.parseBoolean(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to boolean", e);
			}
			
		}
	}

	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a double
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public double callDouble(String method, Object ... params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getDouble("result");
		} 
		catch (JSONException e)
		{
			try {
				return Double.parseDouble(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to double", e);
			}
			
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a double
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public double callDouble(String method, JSONObject params) throws JSONRPCException {
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getDouble("result");
		} 
		catch (JSONException e)
		{
			try {
				return Double.parseDouble(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to double", e);
			}
			
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a JSONObject
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public JSONObject callJSONObject(String method, JSONObject params) throws JSONRPCException {
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getJSONObject("result");
		} 
		catch (JSONException e)
		{
            try {
                if (response.has("result")) {
                    return new JSONObject(response.getString("result"));
                } else{
                    return new JSONObject(response.getString("error"));
                }

            } catch (NumberFormatException | JSONException e1) {
                throw new JSONRPCException("Cannot convert result to JSONObject", e);
            }
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a JSONObject
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public JSONObject callJSONObject(String method, Object ... params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getJSONObject("result");
		} 
		catch (JSONException e)
		{
            try {
                if (response.has("result")) {
                    return new JSONObject(response.getString("result"));
                } else{
                    return new JSONObject(response.getString("error"));
                }

            } catch (NumberFormatException | JSONException e1) {
                throw new JSONRPCException("Cannot convert result to JSONObject", e);
            }
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a JSONArray
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public JSONArray callJSONArray(String method, Object ... params) throws JSONRPCException
	{
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getJSONArray("result");
		} 
		catch (JSONException e)
		{
			try {
				return new JSONArray(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to JSONArray", e);
			}
		}
	}
	
	/**
	 * Perform a remote JSON-RPC method call
	 * @param method The name of the method to invoke
	 * @param params Arguments of the method
	 * @return The result of the RPC as a JSONArray
	 * @throws JSONRPCException if an error is encountered during JSON-RPC method call
	 */
	public JSONArray callJSONArray(String method, JSONObject params) throws JSONRPCException {
		JSONObject response = null;
		try 
		{
			response = doRequest(method, params);
			if(response == null) throw new JSONRPCException("Cannot call method: " + method);

			return response.getJSONArray("result");
		} 
		catch (JSONException e)
		{
			try {
				return new JSONArray(response.getString("result"));
			} catch (NumberFormatException | JSONException e1) {
				throw new JSONRPCException("Cannot convert result to JSONArray", e);
			}
		}
	}
}

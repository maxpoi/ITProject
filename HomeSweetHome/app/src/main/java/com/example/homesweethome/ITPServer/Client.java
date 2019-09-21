package com.example.homesweethome.ITPServer;// A Java program for a Client
import java.net.*;
import java.io.*;

public class Client 
{ 
    // initialize socket and input output streams
	// The IP address will be hard coded for client
	private static final String ip = "10.13.59.131";
	private static final int port = 5000;
    private Socket socket            = null; 
    private DataOutputStream out     = null;
    private DataInputStream in = null;
  
    // constructor for client
    public Client() 
    { 
        // establish a connection 
        try
        { 
            socket = new Socket(ip, port); 
            System.out.println("Connected"); 
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
    

    // The function for checking if the account is already created
    public String checkAccountExist(String email) {
        try {
			out    = new DataOutputStream(socket.getOutputStream());
			String message = String.format("00\\n%s", email);
            out.writeUTF(message); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return getFeedback ();
    }

    // The function called when the user want to create the account
    public String createAccount (String accountName, String email, String password) {
        try {
			out    = new DataOutputStream(socket.getOutputStream());
			String message = String.format("01\\n%s\\n%s\\n%s", 
					accountName, email, password);
            out.writeUTF(message);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return getFeedback ();
    }
    
    // The function called when the user forgets his/her password
    public String forgetPassword(String email) {
        try {
			out    = new DataOutputStream(socket.getOutputStream());
			String message = String.format("02\\n%s", email);
            out.writeUTF(message); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return getFeedback ();
    }
    
    // The function called when the user logins in
    public String login(String email, String password) {
        try {
			out    = new DataOutputStream(socket.getOutputStream());
			String message = String.format("03\\n%s\\n%s", email, password);
            out.writeUTF(message); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return getFeedback ();
    }

    // Get the feedback from the server to give user feedback
    public String getFeedback () {
    	String response = "";
        try {
			response = in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("what i received: "+ response);
        return response;
    }
    
    public void search(String...str) {
    	
    }
} 


/*
request Command:

login  
create
**/
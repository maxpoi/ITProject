//// A Java program for a Client
//import java.net.*;
//import java.io.*;
//import java.util.ArrayList;
//
//public class Client
//{
//    // initialize socket and input output streams
//	// The IP address will be hard coded for client
//	private static final String ip = "10.13.59.131";
//	private static final int port = 5000;
//    private Socket socket            = null;
//    private DataOutputStream out     = null;
//    private DataInputStream in = null;
//    public static Client instance;
//
//    public static Client getInstance() {
//    	if(instance == null)
//    		instance = new Client();
//    	return instance;
//    }
//
//    // constructor for client
//    public Client() {
//        // establish a connection
//        try{
//            socket = new Socket(ip, port);
//            System.out.println("Connected");
//            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//            out = new DataOutputStream(socket.getOutputStream());
//
//        }
//        catch(IOException i) {
//            System.out.println(i);
//        }
//    }
//
//
//    // 00 - The function for checking if the account is already created
//    public String checkAccountExist(String email) {
//    	return this.sendAndReceive (String.format("00\n%s", email));
//    }
//
//    // 01 - The function called when the user want to create the account
//    public String createAccount (String accountName, String email, String password,
//    		String question, String answer) {
//    	return this.sendAndReceive (String.format("01\n%s\n%s\n%s\n%s\n%s",
//				accountName, email, password, question, answer));
//    }
//
//    // 02 - The function called when the user forgets his/her password
//    // ask the database what is the question decided by user
//    public String askQuestion(String email) {
//    	return this.sendAndReceive (String.format("02\n%s", email));
//    }
//
//    // 021 - User typed in the answer and send the answer to database for verifying
//    public String answerQuestion(String email, String answer) {
//    	return this.sendAndReceive (String.format("021\n%s\n%s", email, answer));
//    }
//
//    // 022 - User can change the passwords now by giving their email and answer
//    public String resetPassword(String email, String password) {
//    	return this.sendAndReceive (String.format("022\n%s\n%s", email, password));
//    }
//
//    // 03 - The function called when the user logins in
//    public String login(String email, String password) {
//    	return this.sendAndReceive (String.format("03\n%s\n%s", email, password));
//    }
//
//    // 04 - upload artifact to the server
//    // name, description, video, audio, picture number, picture(s)
//    public String uploadArtifact (Cell artifact) {
//    	StringBuilder message = new StringBuilder();
//
//    	// transfer String information
//    	message.append(artifact.getTitle() + "\n");
//    	message.append(artifact.getDesc() + "\n");
//    	message.append("null" + "\n");
//    	message.append("null" + "\n");
//    	message.append(artifact.getDate() + "\n");
//    	String artifactInfo = null;
//
//    	// transfer images
//    	ArrayList<Image> images = artifact.getImages();
//    	if (images == null) {
//    		message.append("0");
//    		artifactInfo = message.toString();
//    	}
//    	else {
//	    	int numberOfPictures = images.size();
//	    	message.append(String.format("%s", numberOfPictures) + "\n");
//	    	for (int i = 0; i < numberOfPictures; i++) {
//	    		Image image = images.get(i);
//	    		for (int j = 0; j < 3; j++) {
//	    			image.getLowImageString();
//	    			image.getMediumImageString();
//	    			image.getHighImageString();
//	    		}
//	    	}
//	    	artifactInfo = message.toString();
//    	}
//    	return this.sendAndReceive (String.format("04\n%s", artifactInfo));
//    }
//
//    // 05 - delete artifact
//    // number of features, feature name 1, feature 1 new content ...
//    public String deleteArtifact (Cell artifact) {
//    	if (artifact.getImages() == null) {
//    		return "false";
//    	}
//    	return this.sendAndReceive (String.format("05\n%s", artifact.getTitle()));
//    }
//
//    /*
//    // 06 - achieve all artifacts
//    // name, description, video, audio, picture number, picture(s)
//    public ArrayList<Cell> achieveAllArtifact () {
//        ArrayList<Cell> artifacts = new ArrayList<Cell>();
//        String[] received = this.sendAndReceive ("06").split("\n");
//
//    	for (int i = 0; i < received.length; i ++) {
//    		ArrayList<Image> imgs = new ArrayList<Image>();
//    		int numPics = Integer.parseInt(received[i+7]);
//    		int j;
//    		for (j = 0; j < i + 8 + numPics; j++) {
//    			artifact.addImg(received[j]);
//    		}
//    		i = j;
//    		Cell artifact = new Cell (received[i+1], received[i+2], received[i+3],
//    				imgs, received[i+4], received[i+5]);
//    		artifacts.add(artifact);
//    	}
//    	return artifacts;
//    }
//	*/
//
//    // Send message to Server
//    private String sendAndReceive (String message) {
//    	try {
//			out.writeUTF(message);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	return getFeedback ();
//    }
//
//    // Get the feedback from the server to give user feedback
//    private String getFeedback () {
//    	String response = "";
//        try {
//			response = in.readUTF();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.println("what i received: "+ response);
//        return response;
//    }
//
//    public void search(String...str) {
//
//    }
//
//}
//
//
///*
//request Command:
//
//login
//create
//**/
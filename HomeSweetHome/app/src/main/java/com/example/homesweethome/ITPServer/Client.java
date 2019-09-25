//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import com.example.homesweethome.Cell;

public class Client {
    private static final String ip = "10.13.59.131";
    private static final int port = 5000;
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public Client() {
        try {
            this.socket = new Socket("10.13.59.131", 5000);
            System.out.println("Connected");
            this.in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException var2) {
            System.out.println(var2);
        }

    }

    public String checkAccountExist(String email) {
        return this.sendAndReceive(String.format("00\n%s", email));
    }

    public String createAccount(String accountName, String email, String password) {
        return this.sendAndReceive(String.format("01\n%s\n%s\n%s", accountName, email, password));
    }

    public String forgetPassword(String email) {
        return this.sendAndReceive(String.format("02\n%s", email));
    }

    public String login(String email, String password) {
        return this.sendAndReceive(String.format("03\n%s\n%s", email, password));
    }

    public String uploadArtifact(Cell artifact) {
        StringBuilder message = new StringBuilder();
        message.append(artifact.getTitle() + "\n");
        message.append(artifact.getDesc() + "\n");
        message.append("null\n");
        message.append("null\n");
        message.append(artifact.getDate() + "\n");
        message.append("1\n");
        message.append(artifact.getImgs() + "\n");
        String artifactInfo = message.toString();
        return this.sendAndReceive(String.format("04\n%s", artifactInfo));
    }

    public String updateArtifact(Cell artifact) {
        return this.sendAndReceive(String.format("05\n%s", artifact));
    }

    public ArrayList<Cell> achieveAllArtifact() {
        ArrayList<Cell> artifacts = new ArrayList();
        String[] str = this.sendAndReceive("06").split("\n");

        int j;
        for(int i = 0; i < str.length; i = j + 1) {
            ArrayList<String> imgs = new ArrayList();
            Cell artifact = new Cell(str[i + 1], str[i + 2], str[i + 3], imgs, str[i + 4], str[i + 5]);
            int numPics = Integer.parseInt(str[i + 7]);

            for(j = 0; j < i + 8 + numPics; ++j) {
                artifact.addImg(str[j]);
            }

            artifacts.add(artifact);
        }

        return artifacts;
    }

    private String sendAndReceive(String message) {
        try {
            this.out.writeUTF(message);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return this.getFeedback();
    }

    private String getFeedback() {
        String response = "";

        try {
            response = this.in.readUTF();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        System.out.println("what i received: " + response);
        return response;
    }

    public void search(String... str) {
    }
}

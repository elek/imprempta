package net.anzix.imprempta;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DeleteMe {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://www.444.hu");
        URLConnection con = url.openConnection();

        System.out.println(new Scanner((InputStream) con.getContent()).useDelimiter("\\Z").next());


    }
}

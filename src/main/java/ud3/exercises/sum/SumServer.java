package ud3.exercises.sum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SumServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1234);
            Socket connexio = server.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(connexio.getInputStream()));
            PrintWriter out = new PrintWriter(connexio.getOutputStream(), true);

            int a = Integer.parseInt(in.readLine());
            System.out.println("A = " + a);
            int b = Integer.parseInt(in.readLine());
            System.out.println("B = " + b);

            int suma = a + b;
            out.println(suma);

            connexio.close();
            server.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
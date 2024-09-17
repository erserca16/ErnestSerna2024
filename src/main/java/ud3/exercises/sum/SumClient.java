package ud3.exercises.sum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class SumClient {
    public static void main(String[] args) {
        try {
            Scanner kbd = new Scanner(System.in).useLocale(Locale.US);
            Socket socket = new Socket("localhost", 1234);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Introdueix el primer nombre:");
            int a = kbd.nextInt();
            out.println(a);
            System.out.println("Introdueix el segon nombre:");
            int b = kbd.nextInt();
            out.println(b);

            int suma = Integer.parseInt(in.readLine());
            System.out.println("La suma dels dos nombre és: " + suma);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
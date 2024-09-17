package ud1.examples;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Locale;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RunProcessInput {
    public static void main (String[] args) {
        String[] program = {"wsl", "tr", "a", "A"};
        ProcessBuilder pb = new ProcessBuilder(program);
        try {
            Process process = pb.start();

            // Objecte per poder llegir l'entrada estàndard del programa pare
            Scanner in = new Scanner(System.in).useLocale(Locale.US);

            // Objecte per poder escriure a l'entrada estàndard del procés fill
            PrintWriter stdin = new PrintWriter(process.getOutputStream());

            System.out.println("Stdin:");
            String line;
            while(!(line = in.nextLine()).isEmpty())
                stdin.println(line);

            // Quan acabem de introduir dades, cal tancar el stream
            stdin.close();

            // Objectes per poder llegir l'eixida estàndard i l'error
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            int codiRetorn = process.waitFor();
            System.out.println("L'execució de "+ Arrays.toString(program) +" ha acabat amb el codi: "+ codiRetorn);

            System.out.println("Stdout:");
            while ((line = stdout.readLine()) != null)
                System.out.printf("    %s\n", line);

            System.out.println("Stderr:");
            while ((line = stderr.readLine()) != null)
                System.out.printf("    %s\n", line);

        } catch (IOException ex) {
            System.err.println("Excepció d'E/S.");
            System.err.println(ex.getMessage());
            System.exit(-1);
        } catch (InterruptedException ex) {
            System.err.println("El procés fill ha finalitzat de manera incorrecta.");
            System.exit(-1);
        }
    }
}

package ud1.examples;

import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunProcessOutput {
    public static void main (String[] args) {
        String[] program = {"wsl", "echo", "Hello world!"};
        ProcessBuilder pb = new ProcessBuilder(program);
        try {
            Process process = pb.start();
            // Objectes per poder llegir l'eixida estàndard i l'error
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            int codiRetorn = process.waitFor();
            System.out.println("L'execució de "+ Arrays.toString(program) +" ha acabat amb el codi: "+ codiRetorn);

            String line;
            System.out.println("Stdout:");
            while ((line = stdout.readLine()) != null)
                System.out.printf("    %s\n", line);

            System.out.println("Stderr:");
            while ((line = stderr.readLine()) != null)
                System.out.printf("    %s\n", line);



        } catch (IOException ex) {
            System.err.println("Excepció d'E/S:");
            System.err.println(ex.getMessage());
            System.exit(-1);
        } catch (InterruptedException ex) {
            System.err.println("El procés fill ha finalitzat de manera incorrecta.");
            System.exit(-1);
        }
    }
}

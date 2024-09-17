package exam1.twoeditors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TwoEditorsProcess {
    public static void main(String[] args) {
        ProcessBuilder pb1 = new ProcessBuilder("notepad", "files/ud1/text1.txt");
        ProcessBuilder pb2 = new ProcessBuilder("notepad", "files/ud1/text2.txt");
        ProcessBuilder pb3 = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe", "Get-Content", "files/ud1/text1.txt,files/ud1/text2.txt");


        try {
            Process p1 = pb1.start();
            Process p2 = pb2.start();

            p1.waitFor();
            p2.waitFor();
            System.out.println("L'edició de text ha acabat.");

            Process p3 = pb3.start();
            p3.waitFor();
            System.out.println("S'han concatenat els dos fitxers.");

            BufferedReader stdout = new BufferedReader(new InputStreamReader(p3.getInputStream()));
            String line;
            System.out.println("Stdout:");
            while ((line = stdout.readLine()) != null)
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

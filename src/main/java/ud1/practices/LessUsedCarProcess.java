package ud1.practices;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class LessUsedCarProcess {
    public static void main(String[] args) {

        ArrayList<ProcessBuilder> programs = new ArrayList<>();
        programs.add(new ProcessBuilder("wsl", "cat", "files/ud1/concessionari.csv"));
        programs.add(new ProcessBuilder("wsl", "sort", "-t", ",", "-k3", "-n"));
        programs.add(new ProcessBuilder("wsl", "head", "-1"));
        programs.add(new ProcessBuilder("wsl", "cut", "-d", ",", "-f2,3"));

        try {
            List<Process> processes = ProcessBuilder.startPipeline(programs);

            for(Process p : processes)
                p.waitFor();

            Process last = processes.get(processes.size() - 1);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(last.getInputStream()));

            String line;
            System.out.println("Stdout:");
            while ((line = stdout.readLine()) != null)
                System.out.printf("    %s\n", line);

        } catch (IOException ex) {
            System.err.println("Excepció d'E/S.");
            System.out.println(ex.getMessage());
            System.exit(-1);
        } catch (InterruptedException ex) {
            System.err.println("El procés fill ha finalitzat de manera incorrecta.");
            System.exit(-1);
        }
    }
}

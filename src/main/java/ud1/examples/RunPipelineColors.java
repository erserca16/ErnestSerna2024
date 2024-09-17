package ud1.examples;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class RunPipelineColors {
    public static void main (String[] args) {

        ArrayList<ProcessBuilder> programs = new ArrayList<>();
        programs.add(new ProcessBuilder("wsl", "sort", "files/ud1/colors.txt"));
        programs.add(new ProcessBuilder("wsl", "uniq", "-c"));
        programs.add(new ProcessBuilder("wsl", "sort", "-r"));
        programs.add(new ProcessBuilder("wsl", "head", "-3"));

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
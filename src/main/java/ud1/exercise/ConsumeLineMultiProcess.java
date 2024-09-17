package ud1.exercise;

import java.io.IOException;

public class ConsumeLineMultiProcess {
    public static void main(String[] args) {
        ProcessBuilder pbColors = new ProcessBuilder("wsl", "files/ud1/consume_line.sh", "files/ud1/colors.txt", "1").inheritIO();
        ProcessBuilder pbConcessionari = new ProcessBuilder("wsl", "files/ud1/consume_line.sh", "files/ud1/concessionari.csv", "1").inheritIO();

        try {
            Process processColors = pbColors.start();
            Process processConcessionari = pbConcessionari.start();

            processColors.waitFor();
            processConcessionari.waitFor();

            System.out.println("Tots els processos han acabat.");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

package ud2.exercises.ex1;

import java.util.Locale;
import java.util.Scanner;

public class ALotOfThreads {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        System.out.println("Introdueix els nombre de threads a crear:");
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            // Creem un SleepThread
            SleepThread sleepThread = new SleepThread(i);
            sleepThread.start();
        }
    }
}
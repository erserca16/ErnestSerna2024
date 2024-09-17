package ud2.exercises.ex2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class FibonnaciConcurrent {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);

        List<FibonnaciThread> threadList = new ArrayList<>();

        System.out.println("Introdueix els nombres a calcular:");
        int n;
        int i = 1;
        while ((n = sc.nextInt()) != 0){
            FibonnaciThread fibonnaciThread = new FibonnaciThread(i++, n);
            fibonnaciThread.start();
            threadList.add(fibonnaciThread);
        }

        int result = 0;
        for ( FibonnaciThread fibonnaciThread : threadList ) {
            try {
                fibonnaciThread.join();
                result += fibonnaciThread.getResult();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("La suma total és: %d\n", result);
    }
}
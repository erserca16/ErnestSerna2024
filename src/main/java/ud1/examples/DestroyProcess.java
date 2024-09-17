package ud1.examples;

import java.io.IOException;
import java.util.Arrays;

public class DestroyProcess {
    public static void main (String[] args) {
        // Indica la comanda que utilitza aquest programa per iniciar un nou procés
        String[] program = {"powershell", "sleep", "5"};

        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(program);

            System.out.println(
                    "El procés: " + Arrays.toString(program)
                            + (process.isAlive() ? " està viu." : " ha acabat.")
            );
            System.out.println("Destruint...");

            process.destroy();
            process.waitFor();

            System.out.println(
                    "El procés: " + Arrays.toString(program)
                            + (process.isAlive() ? " està viu." : " ha acabat.")
            );
        } catch(IOException ex) {
            System.err.println("Excepció d<E/S");
            System.err.println(ex.getMessage());
            System.exit(-1);
        } catch(InterruptedException ex) {
            System.err.println("El procés fill ha finalitzat de manera incorrecta.");
            System.exit(-1);
        }
    }
}

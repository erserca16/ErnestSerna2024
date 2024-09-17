package pruebas_ordinaria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LeerSalidaProceso {
    public static void main(String[] args) {
        String[] comando = {"ping", "-c", "4", "google.com"};
        ProcessBuilder pb = new ProcessBuilder(comando);

        try {
            Process proceso = pb.start();

            // Usar getInputStream() para leer la salida del proceso
            InputStream inputStream = proceso.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            int codigoSalida = proceso.waitFor();
            System.out.println("El proceso terminó con el código de salida: " + codigoSalida);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
        }
    }
}

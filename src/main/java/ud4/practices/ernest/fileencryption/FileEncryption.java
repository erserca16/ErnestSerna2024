package ud4.practices.ernest.fileencryption;

import java.io.*;
import java.util.Scanner;
import javax.crypto.SecretKey;
public class FileEncryption {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Benvingut a FileEncryption:");
        System.out.println("1) Escriure un fitxer");
        System.out.println("2) Llegir un fitxer");
        System.out.println("0) Eixir");

        int eleccion;
        do {
            System.out.print("Elige una opción: ");
            eleccion = scanner.nextInt();
            scanner.nextLine();

            switch (eleccion) {
                case 1:
                    escribirElFichero();
                    break;
                case 2:
                    leerFichero();
                    break;
                case 0:
                    System.out.println("Adiós, hasta pronto!");
                    break;
                default:
                    System.out.println("Número incorrecto,elige otro");
                    break;
            }
        } while (eleccion != 0);

        scanner.close();
    }

    public static void escribirElFichero() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el nombre del fichero: ");
        String ficheroEscribir = scanner.nextLine();

        File fichero = new File(ficheroEscribir);

        if (fichero.exists()) {
            System.out.print("El fichero existe. Quieres sobreescribirlo? (S/N): ");
            String contestacion = scanner.nextLine();

            if (!contestacion.equalsIgnoreCase("S")) {
                System.out.println("Has elegido no. Elige una opción de nuevo");
                return;
            }
        }

        try {
            System.out.print("Escribe la contraseña que encriptará el archivo: ");
            String contraseña = scanner.nextLine();

            SecretKey secretKey = AES.passwordKeyGeneration(contraseña, 256);

            System.out.println("Escribe el contenido del archivo. Escribe '\\exit' para salir:");

            StringBuilder contenidoArchivo = new StringBuilder();
            String linea;
            while (!(linea = scanner.nextLine()).equals("\\exit")) {
                contenidoArchivo.append(linea).append("\n");
            }

            String contenidoEncriptado = AES.encrypt(secretKey, contenidoArchivo.toString());

            FileWriter fileWriter = new FileWriter(fichero);
            fileWriter.write(contenidoEncriptado);
            fileWriter.close();

            System.out.println("Fichero encriptado correctamente.");
        } catch (Exception e) {
            System.out.println("Error encriptando el fichero: " + e.getMessage());
        }
    }

    public static void leerFichero() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el nombre del fichero que quieres abrir: ");
        String fitxerALlegir = scanner.nextLine();

        File fichero = new File(fitxerALlegir);

        if (!fichero.exists()) {
            System.out.println("El fichero no existe.");
            return;
        }

        try {
            System.out.print("Introduce la contraseña para desencriptar el fichero: ");
            String contraseña = scanner.nextLine();

            SecretKey secretKey = AES.passwordKeyGeneration(contraseña, 256);

            FileReader fileReader = new FileReader(fichero);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder contenidoEncriptado = new StringBuilder();
            String linia;
            while ((linia = bufferedReader.readLine()) != null) {
                contenidoEncriptado.append(linia);
            }
            bufferedReader.close();

            String contenidoDesencriptado = AES.decrypt(secretKey, contenidoEncriptado.toString());

            System.out.println("El contenido del fichero desencriptado es:");
            System.out.println(contenidoDesencriptado);
        } catch (Exception e) {
            System.out.println("Error leyendo el fichero: " + e.getMessage());
        }
    }
}

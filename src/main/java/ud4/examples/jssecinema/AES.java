package ud4.examples.jssecinema;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class AES {

    public static SecretKey keygenKeyGeneration(int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128)||(keySize == 192)||(keySize == 256)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(keySize);
                sKey = kgen.generateKey();

            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Generador no disponible.");
            }
        }
        return sKey;
    }

    public static SecretKey passwordKeyGeneration(String password, int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128)||(keySize == 192)||(keySize == 256)) {
            try {
                byte[] data = password.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(data);
                byte[] key = Arrays.copyOf(hash, keySize/8);
                sKey = new SecretKeySpec(key, "AES");
            } catch (Exception ex) {
                System.err.println("Error generant la clau:" + ex);
            }
        }
        return sKey;
    }

    public static String encrypt(SecretKey key, String str){
        try {
            // Decode the UTF-8 String into byte[] and encrypt it
            byte[] data = encrypt(key, str.getBytes(StandardCharsets.UTF_8));
            // Encode the encrypted data into base64
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception ex){
            System.err.println("Error xifrant les dades: " + ex);
        }
        return null;
    }
    public static String decrypt(SecretKey key, String str){
        try {
            // Decode the base64 encrypted string to a byte[]
            byte[] data = Base64.getDecoder().decode(str);
            // Decrypyt the byte[] data
            byte[] decrypted = decrypt(key, data);
            // Encode the decrypted data in a String
            return new String(decrypted);
        } catch (Exception ex){
            System.err.println("Error desxifrant les dades: " + ex);
        }
        return null;
    }

    public static byte[] encrypt(SecretKey key, byte[] data) throws Exception {
        return aes(key, data, Cipher.ENCRYPT_MODE);
    }
    public static byte[] decrypt(SecretKey key, byte[] data) throws Exception {
        return aes(key, data, Cipher.DECRYPT_MODE);
    }
    private static byte[] aes(SecretKey key, byte[] data, int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(opmode, key);
        return cipher.doFinal(data);
    }

    public static void saveSecretKeyToFile(SecretKey key, String path) throws IOException {
        // Base64 encode the key
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        // Write the key to a file
        Files.write(Paths.get(path), encodedKey.getBytes());
    }

    public static SecretKey loadSecretKeyFromFile(String path) throws IOException {
        // Read the key from file
        String fileContent = new String(Files.readAllBytes(Paths.get(path)));
        // Decode the base64 key into byte[]
        byte[] keyBytes = Base64.getDecoder().decode(fileContent);
        // Create the SecretKey object
        return new SecretKeySpec(keyBytes, "AES");
    }



    public static void main(String[] args) {
        String message = "Aquest és un missatge super secret";
        ArrayList<SecretKey> keys = new ArrayList<>();

        String password = "veryDifficultPassword";
        int keySize = 256;

        keys.add(passwordKeyGeneration(password, keySize));
        System.out.printf("Key generated with password: %s\n", password);

        keys.add(keygenKeyGeneration(keySize));

        for (SecretKey key : keys) {
            System.out.printf("Key: %s\n", Base64.getEncoder().encodeToString(key.getEncoded()));
            System.out.printf("Message: %s\n", message);
            String encrypted = encrypt(key, message);
            System.out.printf("Encrypted message: %s\n", encrypted);
            String decrypted = decrypt(key, encrypted);
            System.out.printf("Decrypted message: %s\n", decrypted);
            System.out.println();
        }
    }
}
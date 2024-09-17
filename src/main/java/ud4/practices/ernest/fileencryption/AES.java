package ud4.practices.ernest.fileencryption;


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
}

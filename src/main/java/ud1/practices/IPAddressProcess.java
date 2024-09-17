package ud1.practices;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressProcess {

    public static void main(String[] args) {


            ProcessBuilder pb = new ProcessBuilder("powershell", "ipconfig");
            try {
                Process process = pb.start();

                BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                int exitCode = process.waitFor();



                String line;
                while ((line = stdout.readLine()) != null) {
                    if (line.contains("Direcci�n IPv4")) {
                        // Use regular expressions to extract the IPv4 address
                        Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String ipv4Address = matcher.group();
                            System.out.println("La direcció IP del dispositiu és: " + ipv4Address);
                        }
                    }
                }

            } catch (IOException ex) {
                System.err.println("Excepció d'E/S.");
                System.out.println(ex.getMessage());
                System.exit(-1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


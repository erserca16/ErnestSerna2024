package ud1.practices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextEditorProcess {

    public static void main(String[] args) {
        String filePath = "files/ud1/text.txt";

        String editorCommand = "notepad " + filePath;

        try {
            Process editorProcess = Runtime.getRuntime().exec(editorCommand);
            int editorExitCode = editorProcess.waitFor();

            if (editorExitCode == 0) {
                System.out.println("Contenido del archivo:");

                showFileContent(filePath);
            } else {
                System.out.println("Error al abrir el editor de texto.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void showFileContent(String filePath) {

        try {
            // també pot ser type + filePath
            Process contentProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "type " + filePath});
            BufferedReader contentReader = new BufferedReader(new InputStreamReader(contentProcess.getInputStream()));

            String line;
            while ((line = contentReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

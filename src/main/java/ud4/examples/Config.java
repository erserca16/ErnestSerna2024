package ud4.examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static Properties getConfig(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));
        return properties;
    }
}

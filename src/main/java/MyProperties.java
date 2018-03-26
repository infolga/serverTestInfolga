import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyProperties {

    private static MyProperties MyProperties;
    Properties prop;

    public static MyProperties instans() {
        if (MyProperties == null) {
            MyProperties = new MyProperties();
        }
        return MyProperties;

    }

    private MyProperties() {

        prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String key, String def) {
        String buf = prop.getProperty(key);
        if (buf == null) {
            return def;
        } else {
            return prop.getProperty(key);
        }
    }
}
